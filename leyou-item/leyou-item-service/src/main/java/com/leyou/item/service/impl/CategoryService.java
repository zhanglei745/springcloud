package com.leyou.item.service.impl;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.CategoryPojo;
import com.leyou.item.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService implements ICategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<CategoryPojo> queryListByPid(Long pid){
        CategoryPojo categoryPojo = new CategoryPojo();
        categoryPojo.setParentId(pid);
        return this.categoryMapper.select(categoryPojo);
    }

    @Override
    public List<String> queryNameByIds(List<Long> ids) {

        List<CategoryPojo> categorys = this.categoryMapper.selectByIdList(ids);

        return categorys.stream().map(categoryPojo -> categoryPojo.getName()).collect(Collectors.toList());

    }

}
