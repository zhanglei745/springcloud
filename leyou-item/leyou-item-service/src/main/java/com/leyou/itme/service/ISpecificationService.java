package com.leyou.itme.service;

import com.leyou.item.pojo.SpecGroup;

import java.util.List;

public interface ISpecificationService {
    List<SpecGroup> querySpecGroupsByCid(Long cid);
}
