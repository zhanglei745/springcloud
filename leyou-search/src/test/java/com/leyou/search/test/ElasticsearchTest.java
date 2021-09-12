package com.leyou.search.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.mapper.GoodsRepository;
import com.leyou.search.pojo.Goods;
import com.leyou.search.service.ISearchService;
import org.checkerframework.checker.units.qual.Acceleration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ElasticsearchTest {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ISearchService searchService;
    @Autowired
    private GoodsClient goodsClient;

    @Test
    public void test(){
        this.elasticsearchTemplate.createIndex(Goods.class);
        this.elasticsearchTemplate.putMapping(Goods.class);
        Integer page = 1;
        Integer rows = 100;
        //循环分页导入数据
        do {
            //分页查询
            PageResult<SpuBo> result = this.goodsClient.querySpuByPage(null, null, page, rows);
            //当前页的数据
            List<SpuBo> items = result.getItems();
            //转换成goods集合
            List<Goods> goodsList = items.stream().map(spuBo -> {
                try {
                    return this.searchService.buildGoods(spuBo);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                return null;
            }).collect(Collectors.toList());
            //新增数据到elasticsearch
            this.goodsRepository.saveAll(goodsList);
            page ++;
            rows = items.size();
        }while (rows ==100);




    }

}
