package com.zd.feign;

import org.springframework.cloud.openfeign.EnableFeignClients;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@EnableFeignClients("com.zd.feign")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableCommonFeignClients {
}
