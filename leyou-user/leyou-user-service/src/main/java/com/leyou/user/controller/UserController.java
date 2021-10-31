package com.leyou.user.controller;

import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Api
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 用户验证
     * @param username
     * @param
     * @return
     */
    @GetMapping("query")
    @ApiOperation(value = "用户验证",notes = "创建地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username",required = true,value = "用户名"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "验证成功"),
            @ApiResponse(code = 400,message = "验证失败，用户没有注册")
    })
    public ResponseEntity<User> queryUser(@RequestParam("username")String username){
        User user = this.userService.queryUser(username,"124");
        if (user == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(user);
    }
}
