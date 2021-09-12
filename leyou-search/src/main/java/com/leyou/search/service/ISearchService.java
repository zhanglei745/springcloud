package com.leyou.search.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Spu;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.query.SearchQuery;

public interface ISearchService {

    public Goods buildGoods(Spu spu) throws JsonProcessingException;

    SearchResult search(SearchQuery query);
}
