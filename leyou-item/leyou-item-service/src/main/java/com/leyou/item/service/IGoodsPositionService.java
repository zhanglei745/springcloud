package com.leyou.item.service;

import com.leyou.item.pojo.GoodsPosition;

import java.util.List;

public interface IGoodsPositionService {
    List<GoodsPosition> queryListByPid(Integer pid);
}
