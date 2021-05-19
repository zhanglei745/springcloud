package com.leyou.item.service.impl;

import com.leyou.item.mapper.GoodsTypeMapper;
import com.leyou.item.pojo.GoodsType;
import com.leyou.item.service.IGoodsType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsTypeService implements IGoodsType {

    @Autowired
    private GoodsTypeMapper goodsTypeMapper;


    @Override
    public List<GoodsType> queryGoodsTypeByPid(Integer pid) {

        GoodsType goodsType = new GoodsType();
        goodsType.setPid(pid);
        return this.goodsTypeMapper.select(goodsType);

    }
}
