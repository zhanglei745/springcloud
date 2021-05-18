package com.leyou.item.mapper;

import com.leyou.item.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {

    @Insert("insert into t_user_role(user_id ,role_id) values (#{uid},#{rid})")
    void insertUserRoles(@Param("uid") Long uid, @Param("rid") Long rid);
}
