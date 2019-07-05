package com.zd.geteway;

import com.zd.core.bean.ResultBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringCloudApplication
public class GetewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GetewayApplication.class, args);
    }

    @RequestMapping("/hystrixTimeOut")
    public ResultBean hystrixTimeOut() {
        return ResultBean.builder().code("500").msg("熔断").build();
    }
}
