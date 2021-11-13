package com.leyou.goods.service;

import com.leyou.goods.client.BrandClient;
import com.leyou.goods.client.CategoryClient;
import com.leyou.goods.client.GoodsClient;
import com.leyou.goods.client.SpecificationClient;
import com.leyou.item.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GoodsService {
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecificationClient specificationClient;


    public Map<String,Object> loadData(Long spuId){
        Map<String,Object> model = new HashMap<>();
        Spu spu = this.goodsClient.querySpuById(spuId);
        SpuDetail spuDetail = this.goodsClient.querySpuDetailById(spuId);
        List<Long> cids = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
        List<String> names = this.categoryClient.queryNamesByIds(cids);
        List<Map<String,Object>> categorys = new ArrayList<>();
        for (int i= 0;i<cids.size();i++){
            Map<String,Object> category = new HashMap<>();
            category.put("id",cids.get(i));
            category.put("name",names.get(i));

            categorys.add(category);
        }
        BrandPojo brandPojo = this.brandClient.queryBrandById(spu.getBrandId());

        List<Sku> skus = this.goodsClient.querySkusBySpuId(spuId);
        List<SpecGroup> specGroups = this.specificationClient.queryGroupWithParam(spu.getCid3());
        List<SpecParam> specParams = this.specificationClient.querySeceParam(null, spu.getCid3(), false, null);

        //特殊规格参数
        Map<Long,String> paramMap = new HashMap<>();
        specParams.forEach(specParam -> {
            paramMap.put(specParam.getId(),specParam.getName());
        });

        model.put("spu",spu);
        model.put("spuDetail",spuDetail);
        model.put("categories",categorys);
        model.put("brand",brandPojo);
        model.put("skus",skus);
        model.put("groups",specGroups);
        model.put("paramMap",paramMap);

        return model;
    }

}
