package com.zd.feign.api.user;

import com.zd.core.bean.ResultBean;
import com.zd.entity.user.AuthUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "authorized", path = "/user")
public interface UserApi {

    @GetMapping("/{userId}")
    ResultBean<AuthUser> getUserById(@PathVariable Integer userId);


}
