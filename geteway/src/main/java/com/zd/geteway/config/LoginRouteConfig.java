package com.zd.geteway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoginRouteConfig {

    public RouteLocator loginRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/login").uri("localhost:23001"))
                .build();
    }

}
