package com.leyou.item.service;

import com.leyou.item.pojo.CategoryPojo;

import java.util.List;

public interface ICategoryService {

    List<CategoryPojo> queryListByPid(Long pid);


}
