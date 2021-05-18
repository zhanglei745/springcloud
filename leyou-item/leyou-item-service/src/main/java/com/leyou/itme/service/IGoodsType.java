package com.leyou.itme.service;

import com.leyou.item.pojo.GoodsType;

import java.util.List;

public interface IGoodsType {
//    List<GoodsType> queryGoodsTypeBylevel(Integer level);

    List<GoodsType> queryGoodsTypeByPid(Integer pid);
}
