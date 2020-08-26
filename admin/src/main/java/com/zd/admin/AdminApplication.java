package com.zd.admin;

import com.zd.core.interfaces.EnableCommonScan;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;

@EnableAdminServer
@SpringCloudApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

}
