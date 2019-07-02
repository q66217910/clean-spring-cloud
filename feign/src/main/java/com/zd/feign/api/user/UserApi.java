package com.zd.feign.api.user;

import com.zd.core.bean.ResultBean;
import com.zd.feign.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user", path = "/user")
public interface UserApi {

    @GetMapping("/{userId}")
    ResultBean getUserByIdWithResult(@PathVariable Integer userId);

    @GetMapping("/{userId}")
    User getUserById(@PathVariable Integer userId);

}
