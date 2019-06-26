package com.zd.user.controller;

import com.zd.core.bean.ResultBean;
import com.zd.feign.api.user.UserApi;
import com.zd.feign.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController implements UserApi {

    @GetMapping("/{userId}")
    public ResultBean<User> getUserById(@PathVariable Integer userId) {
        User user = new User();
        user.setUserId(1);
        user.setUserName("");
        return ResultBean.<User>builder().code("0").msg("").data(user).build();
    }

}