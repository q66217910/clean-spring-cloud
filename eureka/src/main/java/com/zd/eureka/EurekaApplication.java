package com.zd.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@EnableEurekaServer
@SpringBootApplication
public class EurekaApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class, args);
    }

}
