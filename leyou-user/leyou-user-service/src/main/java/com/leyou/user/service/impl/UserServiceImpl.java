package com.leyou.user.service.impl;

import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户验证
     * @param username
     * @param password
     * @return
     */
    @Override
    public User queryUser(String username, String password) {
        /**
         * 逻辑改变，先去缓存中查询用户数据，查到的话直接返回，查不到再去数据库中查询，然后放入到缓存当中
         */
        User record = new User();
        record.setUsername(username);
        User user = this.userMapper.selectOne(record);
        return user;
    }
}
