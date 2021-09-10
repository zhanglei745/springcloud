package com.leyou.search.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.leyou.item.pojo.Spu;
import com.leyou.search.pojo.Goods;

public interface ISearchService {

    public Goods buildGoods(Spu spu) throws JsonProcessingException;
}
