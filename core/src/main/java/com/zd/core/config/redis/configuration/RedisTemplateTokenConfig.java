package com.zd.core.config.redis.configuration;

import com.zd.core.config.redis.RedisConfig;
import com.zd.core.config.redis.template.RedisTemplateToken;
import com.zd.core.redis.RedisTemplateClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisTemplateTokenConfig {

    @Bean
    @ConfigurationProperties("redis.config.token")
    public RedisConfig tokenRedisConfig() {
        return new RedisConfig();
    }

    @Bean
    public RedisTemplateToken tokenRedisTemplate(RedisConfig tokenRedisConfig) {
        return new RedisTemplateToken().factory(tokenRedisConfig);
    }

}
