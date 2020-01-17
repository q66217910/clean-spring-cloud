package com.zd.user.controller;

import com.zd.core.bean.ResultBean;
import com.zd.core.config.redis.template.RedisTemplateUser;
import com.zd.entity.user.AuthUser;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final RedisTemplateUser<String, AuthUser> redisTemplateUser;

    public UserController(RedisTemplateUser<String, AuthUser> redisTemplateUser) {
        this.redisTemplateUser = redisTemplateUser;
    }

    @GetMapping("/{userId}")
    public ResultBean<AuthUser> getUserById(@PathVariable Integer userId) {
        AuthUser user = new AuthUser();
        user.setUserId(userId);
        return ResultBean.success(redisTemplateUser.opsForClass().getClass(user));
    }

    @PostMapping("/user")
    public ResultBean<AuthUser> saveUser(@RequestBody AuthUser user) {
        redisTemplateUser.opsForClass().putClass(user);
        return ResultBean.success();
    }

}
