package com.leyou.item.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.User;

import java.util.List;

public interface IUserService {

    PageResult<User> queryUsers(String key, Integer page, Integer rows, String sortBy, Boolean desc);

    void saveUser(User user, List<Long> rids);
}
