package com.zd.feign.api;

import com.zd.core.bean.ResultBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("example")
public interface ExampleApi {

    @GetMapping("/example")
    ResultBean<String> getExample();
}
