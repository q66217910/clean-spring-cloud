package com.zd.admin;

import com.zd.core.config.common.WebSecurityConfig;
import com.zd.core.interfaces.EnableCommonScan;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.Import;

@EnableAdminServer
@Import(WebSecurityConfig.class)
@SpringCloudApplication
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

}
