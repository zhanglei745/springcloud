package com.leyou.search.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.*;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.mapper.GoodsRepository;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.query.SearchQuery;
import com.leyou.search.service.ISearchService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.checkerframework.checker.units.qual.A;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService implements ISearchService {


    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private BrandClient brandClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecificationClient specificationClient;
    @Autowired
    private GoodsRepository goodsRepository;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public Goods buildGoods(Spu spu) throws IOException {
        Goods goods = new Goods();

        //根据分类id获取分类名称
        List<String> names = this.categoryClient.queryNamesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));

        //根据品牌id获取品牌名称
        BrandPojo brandPojo = this.brandClient.queryBrandById(spu.getBrandId());

        //根据spu的id获取所有的sku
        List<Sku> skus = this.goodsClient.querySkusBySpuId(spu.getId());
        List<Long> prices = new ArrayList<>();
        //搜集sku的必要字段,筛选字段是为了减少网络开销
        List<Map<String,Object>> skuMapList = new ArrayList<>();
        skus.forEach(sku -> {
            prices.add(sku.getPrice());
            Map<String,Object> map = new HashMap<>();
            map.put("id",sku.getId());
            map.put("title",sku.getTitle());
            map.put("price",sku.getPrice());
            //判断图片是否为空，不为空就按照"，"切割图片，获取第一张
            map.put("image",StringUtils.isBlank(sku.getImages())?"":StringUtils.split(sku.getImages(),",")[0]);
            skuMapList.add(map);
        });
        //根据cid3查询所有的搜索规格参数
        List<SpecParam> specParams = this.specificationClient.querySeceParam(null, spu.getCid3(), null, true);
        //根据spuid查询详情
        SpuDetail spuDetail = this.goodsClient.querySpuDetailById(spu.getId());
        //反序列化规格参数 TypeReference  泛型参数可以序列化成任意对象类型
        Map<String, Object> genericSpecMap = MAPPER.readValue(spuDetail.getGenericSpec(), new TypeReference<Map<String, Object>>() {

        });
        //反序列化特殊规格参数
        Map<String, List<Object>> specialSpecMap = MAPPER.readValue(spuDetail.getSpecialSpec(), new TypeReference<Map<String, List<Object>>>() {
        });

        Map<String,Object> specs = new HashMap<>();
        specParams.forEach(specParam->{
            if(specParam.getGeneric()){//判断规格参数是否是通用的规格参数
                //通用类型参数获取
                String value = genericSpecMap.get(specParam.getId().toString()).toString();
                if(specParam.getNumeric()){//数值类型的参数，需要判断区间
                    value = chooseSegment(value, specParam);
                }
                specs.put(specParam.getName(),value);
            }else{
                //特殊规格参数获取
                List<Object> value = specialSpecMap.get(specParam.getId().toString());
                specs.put(specParam.getName(),value);
            }
        });


        goods.setId(spu.getId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setBrandId(spu.getBrandId());
        goods.setSubTitle(spu.getSubTitle());
        //all 字段需要 分类名称和品牌名称
        goods.setAll(spu.getTitle()+" "+ StringUtils.join(names," ")+" "+brandPojo.getName());
        goods.setCreateTime(spu.getCreateTime());
        goods.setPrice(prices);//获取所有spu下的sku的价格
        goods.setSkus(MAPPER.writeValueAsString(skuMapList));//获取所有spu下的sku，转化成json
        goods.setSpecs(specs);//获取所有的查询的 规格参数  规格参数名：值

        return goods;
    }

    @Override
    public SearchResult search(SearchQuery query) {

        if(StringUtils.isBlank(query.getKey())){
            return null;
        }

        //自定义查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //添加查询条件
//        MatchQueryBuilder baseicQuery = QueryBuilders.matchQuery("all", query.getKey()).operator(Operator.AND);
        BoolQueryBuilder baseicQuery = buildBoolQueryBuilder(query);
        queryBuilder.withQuery(baseicQuery);
        //添加分页,分页页面从0开始，此处页码要 -1
        queryBuilder.withPageable(PageRequest.of(query.getPage()-1,query.getSize()));
        //添加结果过滤,包含三个字段，其他排除
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","skus","subTitle"},null));

        //添加分类和品牌的聚合
        String categoriesAggName = "categories";
        String brandsAggName = "brands";
        queryBuilder.addAggregation(AggregationBuilders.terms(categoriesAggName).field("cid3"));
        queryBuilder.addAggregation(AggregationBuilders.terms(brandsAggName).field("brandId"));

        //执行查询，获取结果集
        AggregatedPage<Goods> goodsPage = (AggregatedPage<Goods>)goodsRepository.search(queryBuilder.build());
        //解析聚合后的结果集

        List<Map<String,Object>> categories = getCategoriesAggResult(goodsPage.getAggregation(categoriesAggName));
        List<BrandPojo> brands = getBrandsAggResult(goodsPage.getAggregation(brandsAggName));

        List<Map<String,Object>> specs = null;
        //判断是否一个分类，只有一个分类才能做规格参数聚合
        if(!CollectionUtils.isEmpty(categories) && categories.size()==1){
            //对规格参数做聚合
            specs = getParamsAggResult((Long)categories.get(0).get("Id"),baseicQuery);
        }

        return new SearchResult(goodsPage.getTotalElements(),goodsPage.getTotalPages(),goodsPage.getContent(),categories,brands,specs);
    }

    /**
     * 构建过滤查询条件
     * @param query
     * @return
     */
    private BoolQueryBuilder buildBoolQueryBuilder(SearchQuery query){
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //添加基本查询条件
        boolQueryBuilder.must(QueryBuilders.matchQuery("all",query.getKey()).operator(Operator.AND));
        Map<String, Object> filter = query.getFilter();
        for (Map.Entry<String, Object> entry : filter.entrySet()) {
            String key = entry.getKey();
            if(StringUtils.equals("品牌",key)){
                key = "brandId";
            }else if(StringUtils.equals("分类",key)){
                key = "cid3";
            }else{
                key = "specs."+key+".keyword";
            }
            boolQueryBuilder.filter(QueryBuilders.termQuery(key,entry.getValue()));
        }
        return boolQueryBuilder;
    }

    /**
     * 根据查询条件做规格参数查询
     * @param cid
     * @param baseicQuery
     * @return
     */
    private List<Map<String,Object>> getParamsAggResult(Long cid,BoolQueryBuilder baseicQuery){
        //自定义查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //添加查询条件
        queryBuilder.withQuery(baseicQuery);

        //查询要聚合的规格参数
        List<SpecParam> specParams = this.specificationClient.querySeceParam(null,cid,null,true);
        //添加规格参数聚合
        specParams.forEach(param->{
            queryBuilder.addAggregation(AggregationBuilders.terms(param.getName()).field("specs."+param.getName()+".keyword"));
        });
        //添加结果集过滤
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{},null));
        //执行聚合
        AggregatedPage<Goods> pageGoods = (AggregatedPage<Goods>)this.goodsRepository.search(queryBuilder.build());
        List<Map<String,Object>> specs = new ArrayList<>();
        //解析聚合,key 聚合名称(规格参数名称)，value 聚合对象
        Map<String, Aggregation> aggregationMap = pageGoods.getAggregations().asMap();
        for (Map.Entry<String, Aggregation> entry : aggregationMap.entrySet()) {
            Map<String,Object> map = new HashMap<>();//初始化，key是规格参数，value是规格参数值
            map.put("k",entry.getKey());
            List<String> options = new ArrayList<>();//初始化一个options集合，收集桶中的key
            //获取聚合
            try{
                StringTerms terms = (StringTerms)entry.getValue();
                //获取桶集合
                terms.getBuckets().forEach(bucket->{
                    options.add(bucket.getKeyAsString());
                });
                map.put("options",options);
                specs.add(map);
            }catch (Exception e){
                //不处理string以外的terms
            }

        }
        return specs;
    }




    private List<Map<String,Object>> getCategoriesAggResult(Aggregation aggregation){
        LongTerms terms = (LongTerms) aggregation;
        //获取集合中的桶
        return terms.getBuckets().stream().map(bucket->{
            Map<String,Object> map = new HashMap<>();
            Long id = bucket.getKeyAsNumber().longValue();
            List<String> names = this.categoryClient.queryNamesByIds(Arrays.asList(id));
            map.put("id",id);
            map.put("name",names.get(0));
            return map;
        }).collect(Collectors.toList());
    }
    private List<BrandPojo> getBrandsAggResult(Aggregation aggregation){
        LongTerms terms = (LongTerms) aggregation;
        //获取集合中的桶
        return terms.getBuckets().stream().map(bucket->{
            return this.brandClient.queryBrandById(bucket.getKeyAsNumber().longValue());

        }).collect(Collectors.toList());
    }

    private String chooseSegment(String value,SpecParam specParam){
        Double val = NumberUtils.toDouble(value);
        String result = "其他";

        for(String segment:specParam.getSegments().split(",")){
            String[] segs = segment.split("-");
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length==2){
                end = NumberUtils.toDouble(segs[1]);
            }
            //判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length ==1){
                    result = segs[0] + specParam.getUnit() + "以上";
                }else if(begin ==0){
                    result = segs[1] + specParam.getUnit() + "以下";
                }else{
                    result = segment + specParam.getUnit();
                }
                break;
            }
        }
        return  result;
    }


}
