package com.leyou.itme.service.impl;

import com.leyou.item.mapper.GoodsPositionMapper;
import com.leyou.item.pojo.GoodsPosition;
import com.leyou.itme.service.IGoodsPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsPositionService implements IGoodsPositionService {

    @Autowired
    private GoodsPositionMapper goodsPositionMapper;


    @Override
    public List<GoodsPosition> queryListByPid(Integer pid) {
        GoodsPosition goodsPosition = new GoodsPosition();
        goodsPosition.setPid(pid);
        return this.goodsPositionMapper.select(goodsPosition);
    }
}
