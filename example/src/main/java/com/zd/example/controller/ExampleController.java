package com.zd.example.controller;

import com.zd.core.bean.ResultBean;
import com.zd.entity.user.AuthUser;
import com.zd.feign.api.ExampleApi;
import com.zd.feign.api.user.UserApi;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.cloud.sleuth.instrument.async.TraceableExecutorService;
import org.springframework.integration.annotation.IdempotentReceiver;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/example")
public class ExampleController implements ExampleApi {

    private final UserApi userApi;

    public ExampleController(UserApi userApi) {
        this.userApi = userApi;
    }


    @GetMapping("/example")
    public ResultBean<String> getExample() {
        return ResultBean.<String>builder().data("a").build();
    }

    @Override
    public CompletableFuture getExample1() {
        return null;
    }

    @GetMapping("/user")
    public ResultBean<AuthUser> getUser() {
        return userApi.getUserById(1);
    }
}
