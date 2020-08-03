package com.zd.user;

import com.zd.core.config.mysql.datasouces.AuthDatasource;
import com.zd.core.config.redis.configuration.RedisTemplateTokenConfig;
import com.zd.core.config.redis.configuration.RedisTemplateUserConfig;
import com.zd.core.interfaces.EnableCommonScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;


@SpringCloudApplication
@EnableConfigurationProperties
@EnableAuthorizationServer
@EnableCommonScan
@Import({RedisTemplateTokenConfig.class, RedisTemplateUserConfig.class, AuthDatasource.class})
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}

