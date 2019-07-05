package com.zd.user.controller;

import com.zd.core.bean.ResultBean;
import com.zd.core.config.redis.template.RedisTemplateUser;
import com.zd.feign.api.user.UserApi;
import com.zd.feign.entity.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController implements UserApi {

    private final RedisTemplateUser<String, User> redisTemplateUser;

    public UserController(RedisTemplateUser<String, User> redisTemplateUser) {
        this.redisTemplateUser = redisTemplateUser;
    }

    @GetMapping("/{userId}")
    public ResultBean<User> getUserById(@PathVariable Integer userId) {
        User user = new User();
        user.setUserId(userId);
        return ResultBean.<User>builder().code("0").msg("").data(redisTemplateUser.opsForClass().getClass(user)).build();
    }

    @PostMapping("/user")
    public ResultBean<User> saveUser(@RequestBody User user) {
        redisTemplateUser.opsForClass().putClass(user);
        return ResultBean.<User>builder().code("0").msg("").build();
    }

}
