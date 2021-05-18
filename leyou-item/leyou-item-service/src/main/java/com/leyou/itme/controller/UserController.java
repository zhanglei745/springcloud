package com.leyou.itme.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.RolePojo;
import com.leyou.item.pojo.User;
import com.leyou.itme.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("user")
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping("page")
    public ResponseEntity<PageResult<User>> queryUsers(@RequestParam(value = "key",required = false)String key,
                                                       @RequestParam(value = "page",defaultValue = "1")Integer page,
                                                       @RequestParam(value = "rows",defaultValue = "5")Integer rows,
                                                       @RequestParam(value = "sortBy",required = false)String sortBy,
                                                       @RequestParam(value = "desc",required = false)Boolean desc){

        PageResult<User> users = this.userService.queryUsers(key,page,rows,sortBy,desc);
        if(CollectionUtils.isEmpty(users.getItems())){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(users);


    }


    //新增用户，新增用户的时候需要给用户相应的角色
    @PostMapping
    public ResponseEntity<Void> saveUser(User user, @RequestParam(value = "rids") List<Long> rids){
        this.userService.saveUser(user,rids);
        return ResponseEntity.status(HttpStatus.CREATED).build();//返回201

    }


}
