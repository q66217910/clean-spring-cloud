package com.zd.user.controller;

import com.zd.core.bean.ResultBean;
import com.zd.core.config.redis.template.RedisTemplateUser;
import com.zd.entity.user.AuthUser;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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

    @RequestMapping("/me")
    public Principal saveUser(Principal principal) {
        return principal;
    }

}
