package com.leyou.item.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.BrandPojo;

import java.util.List;

public interface IBrandService {
    PageResult<BrandPojo> queryBrands(String key, Integer page, Integer rows, String sortBy, Boolean desc);

    void save(BrandPojo brand, List<Long> cids);
}
