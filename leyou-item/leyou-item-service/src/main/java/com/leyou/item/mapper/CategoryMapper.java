package com.leyou.item.mapper;

import com.leyou.item.pojo.CategoryPojo;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface CategoryMapper extends Mapper<CategoryPojo>, SelectByIdListMapper<CategoryPojo,Long> {

}
