package com.leyou.itme.service.impl;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.CategoryPojo;
import com.leyou.itme.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

}
