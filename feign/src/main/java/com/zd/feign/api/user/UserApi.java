package com.zd.feign.api.user;

import com.zd.core.bean.ResultBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user", path = "/user")
public interface UserApi {

    @GetMapping("/{userId}")
    ResultBean<User> getUserById(@PathVariable Integer userId);


}
