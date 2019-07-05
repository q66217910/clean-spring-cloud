package com.zd.user;

import com.zd.core.config.redis.template.RedisTemplateToken;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;


@ComponentScan(basePackages = "com.zd")
@SpringCloudApplication
@EnableConfigurationProperties
@EnableAuthorizationServer
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

}
