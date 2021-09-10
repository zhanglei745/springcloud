package com.leyou.search.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.item.pojo.*;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.service.ISearchService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public Goods buildGoods(Spu spu) throws JsonProcessingException {
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
