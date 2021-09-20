package com.leyou.user.service;

import com.leyou.user.pojo.User;

public interface UserService {

    /**
     * 用户验证
     * @param username
     * @param password
     * @return
     */
    User queryUser(String username, String password);

}
