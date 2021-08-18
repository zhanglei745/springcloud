package com.leyou.item.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;

public interface IGoodsService {

    PageResult<SpuBo> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows);
}
