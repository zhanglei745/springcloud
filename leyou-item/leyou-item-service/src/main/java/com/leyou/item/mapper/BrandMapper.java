package com.leyou.item.mapper;

import com.leyou.item.pojo.BrandPojo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface BrandMapper extends Mapper<BrandPojo> {
    @Insert("insert into tb_category_brand(brand_id,category_id) values(#{bid},#{cid})")
    void insertBrandCategorys(@Param("bid") Long bid,@Param("cid") Long cid);
}
