package com.zd.feign.api;

import com.zd.core.bean.ResultBean;
import com.zd.feign.fallback.MyFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.concurrent.CompletableFuture;

@FeignClient(value = "example",fallbackFactory = MyFallbackFactory.class)
public interface ExampleApi {

    @GetMapping("/example")
    ResultBean<String> getExample();

    CompletableFuture getExample1();
}
