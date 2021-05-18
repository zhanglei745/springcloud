package com.leyou.itme.service;

import com.leyou.item.pojo.GoodsPosition;

import java.util.List;

public interface IGoodsPositionService {
    List<GoodsPosition> queryListByPid(Integer pid);
}
