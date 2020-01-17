package com.zd.core.config.common;

import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationName {

    private final ApplicationContext applicationContext;

    public ApplicationName(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 首页
     *
     * @return
     */
    @GetMapping("/")
    public String index() {
        return applicationContext.getEnvironment().getProperty("spring.application.name");
    }
}
