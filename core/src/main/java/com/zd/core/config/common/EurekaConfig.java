package com.zd.core.config.common;

import com.netflix.discovery.AbstractDiscoveryClientOptionalArgs;
import org.springframework.cloud.netflix.eureka.http.RestTemplateDiscoveryClientOptionalArgs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EurekaConfig {

    @Bean
    public AbstractDiscoveryClientOptionalArgs<?> optionalArgs() {
        return new RestTemplateDiscoveryClientOptionalArgs();
    }

}
