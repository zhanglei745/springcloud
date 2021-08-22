package com.leyou.item.mapper;

import com.leyou.item.pojo.BrandPojo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<BrandPojo> {
    @Insert("insert into tb_category_brand(brand_id,category_id) values(#{bid},#{cid})")
    void insertBrandCategorys(@Param("bid") Long bid,@Param("cid") Long cid);

    @Select("select * from tb_brand tb inner join tb_category_brand tcb on tb.id = tcb.brand_id where tcb.category_id = #{cid}")
    List<BrandPojo> selectBrandByCid(Long cid);
}
