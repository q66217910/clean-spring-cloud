package com.zd.user.config;

import com.google.common.collect.Lists;
import com.zd.core.config.redis.template.RedisTemplateUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class UserDetailConfig {

    private final RedisTemplateUser<Integer, com.zd.feign.entity.User> templateUser;

    public UserDetailConfig(RedisTemplateUser<Integer, com.zd.feign.entity.User> templateUser) {
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

    /**
     * 全局用户信息
     *
     * @param auth 认证管理
     * @throws Exception 用户认证异常信息
     */
    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(new BCryptPasswordEncoder());
    }


}
