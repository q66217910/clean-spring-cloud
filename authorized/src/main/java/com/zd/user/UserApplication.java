package com.zd.user;

import com.zd.core.config.redis.configuration.RedisTemplateTokenConfig;
import com.zd.core.config.redis.configuration.RedisTemplateUserConfig;
import com.zd.core.config.redis.template.RedisTemplateToken;
import com.zd.core.config.redis.template.RedisTemplateUser;
import com.zd.core.filter.type.ConfigFilter;
import com.zd.core.interfaces.EnableCommonScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@SpringCloudApplication
@EnableConfigurationProperties
@EnableAuthorizationServer
@EnableCommonScan
@Import({RedisTemplateTokenConfig.class, RedisTemplateUserConfig.class})
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}

