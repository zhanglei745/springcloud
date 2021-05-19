package com.leyou.item.service.impl;

import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.service.ISpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecificationService implements ISpecificationService {


    @Autowired
    private SpecGroupMapper specGroupMapper;
    @Autowired
    private SpecParamMapper specParamMapper;


    @Override
    public List<SpecGroup> querySpecGroupsByCid(Long cid) {
        SpecGroup recode = new SpecGroup();
        recode.setCid(cid);
        return this.specGroupMapper.select(recode);
    }
}
