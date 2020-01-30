package com.zd.feign;

import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients("com.zd.feign")
public @interface EnableCommonFeignClients {
}
