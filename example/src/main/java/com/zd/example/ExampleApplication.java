package com.zd.example;

import com.zd.core.config.common.SwaggerConfig;
import com.zd.core.config.common.WebMvcConfig;
import com.zd.core.config.mysql.datasouces.AuthDatasource;
import com.zd.core.config.redis.configuration.RedisTemplateTokenConfig;
import com.zd.core.config.redis.configuration.RedisTemplateUserConfig;
import com.zd.core.interfaces.EnableCommonScan;
import com.zd.core.interfaces.EnableStream;
import com.zd.feign.EnableCommonFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@SpringCloudApplication
@EnableCommonScan
@EnableCommonFeignClients
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class ExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }

}
