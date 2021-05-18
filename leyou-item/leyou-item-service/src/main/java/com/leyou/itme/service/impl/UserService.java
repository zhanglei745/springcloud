package com.leyou.itme.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.mapper.UserMapper;
import com.leyou.item.pojo.User;
import com.leyou.itme.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.persistence.Transient;
import java.util.List;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    @Transient
    public void saveUser(User user, List<Long> rids) {

        this.userMapper.insertSelective(user);

        rids.forEach(rid->{
            this.userMapper.insertUserRoles(user.getId(),rid);
        });

    }

    @Override
    public PageResult<User> queryUsers(String key, Integer page, Integer rows, String sortBy, Boolean desc) {

        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();

        if(StringUtil.isNotEmpty(key)){
            criteria.andLike("name","%"+key+"%").orLike("username","%"+key+"%");
        }

        //添加分页条件
        PageHelper.startPage(page,rows);

        //添加排序
        if(StringUtil.isNotEmpty(sortBy)){
            example.orderBy(sortBy+" "+ (desc ?"asc":"desc"));
        }

        List<User> users = this.userMapper.selectByExample(example);

        PageInfo<User> pageInfo = new PageInfo<>(users);
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getList());
    }
}
