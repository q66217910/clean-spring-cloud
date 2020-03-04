package com.zd.example;

import com.zd.core.config.common.SwaggerConfig;
import com.zd.core.config.common.WebMvcConfig;
import com.zd.core.interfaces.EnableCommonScan;
import com.zd.core.interfaces.EnableStream;
import com.zd.feign.EnableCommonFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringCloudApplication
@EnableCommonScan
@EnableCommonFeignClients
public class ExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }

}
