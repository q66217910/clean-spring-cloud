package com.zd.example.controller;

import com.zd.core.bean.ResultBean;
import com.zd.feign.api.ExampleApi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/example")
public class ExampleController implements ExampleApi {

    @GetMapping("/example")
    public ResultBean<String> getExample() {
        return ResultBean.<String>builder().data("a").build();
    }
}
