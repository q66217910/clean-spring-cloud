package com.zd.core.config.redis.configuration;

import com.zd.core.config.redis.RedisConfig;
import com.zd.core.config.redis.template.RedisTemplateToken;
import com.zd.core.config.redis.template.RedisTemplateUser;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisTemplateUserConfig {

    @Bean
    @ConfigurationProperties("redis.config.user")
    public RedisConfig userRedisConfig() {
        return new RedisConfig();
    }

    @Bean
    public RedisTemplateUser userRedisTemplate(RedisConfig userRedisConfig) {
        return new RedisTemplateUser<>().factory(userRedisConfig);
    }

}
