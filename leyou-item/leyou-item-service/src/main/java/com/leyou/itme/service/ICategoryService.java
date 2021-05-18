package com.leyou.itme.service;

import com.leyou.item.pojo.CategoryPojo;

import java.util.List;

public interface ICategoryService {

    public List<CategoryPojo> queryListByPid(Long pid);


}
