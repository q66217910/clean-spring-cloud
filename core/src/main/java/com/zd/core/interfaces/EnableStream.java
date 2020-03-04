package com.zd.core.interfaces;


import org.springframework.context.annotation.ComponentScan;

import java.lang.annotation.*;

@Documented
@Inherited  
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ComponentScan
@ComponentScan("com.zd.feign.stream")
public @interface EnableStream {
}
