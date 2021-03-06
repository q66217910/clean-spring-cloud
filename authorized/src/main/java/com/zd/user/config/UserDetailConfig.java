package com.zd.user.config;

import com.google.common.collect.Lists;
import com.zd.core.config.redis.template.RedisTemplateUser;
import com.zd.entity.user.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class UserDetailConfig {

    private final RedisTemplateUser<Integer, AuthUser> templateUser;

    public UserDetailConfig(RedisTemplateUser<Integer, AuthUser> templateUser) {
        this.templateUser = templateUser;
    }

    /**
     * 注入用户信息服务
     *
     * @return 用户信息服务对象
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return userName -> new User(userName, new BCryptPasswordEncoder().encode("123456"), Lists.newArrayList());
    }
}
