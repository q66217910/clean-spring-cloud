package com.zd.zuul;

import com.zd.core.filter.type.ConfigFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;


@EnableZuulProxy
@SpringCloudApplication
@ComponentScan(basePackages = "com.zd",
        excludeFilters = {@ComponentScan.Filter(type = FilterType.CUSTOM, value = {ConfigFilter.class})})
public class ZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class, args);
    }


}
