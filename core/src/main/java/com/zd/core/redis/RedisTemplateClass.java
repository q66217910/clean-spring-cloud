package com.zd.core.redis;

import com.zd.core.config.redis.RedisConfig;
import com.zd.core.redis.operations.impl.ClassOperationsImpl;
import com.zd.core.redis.serializer.JacksonRedisSerializer;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

public class RedisTemplateClass<H, HK, HV> extends RedisTemplate<HK, HV> {

    public RedisTemplateClass() {
        this.setKeySerializer(RedisSerializer.string());
        this.setHashKeySerializer(RedisSerializer.string());
        this.setValueSerializer(new JacksonRedisSerializer<>());
        this.setHashValueSerializer(new JacksonRedisSerializer<>());
    }

    public RedisTemplateClass factory(RedisConfig redisConfig) {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisConfig.getHost(), redisConfig.getPort());
        configuration.setPassword(redisConfig.getPassword());
        configuration.setDatabase(redisConfig.getDb());
        this.setConnectionFactory(new LettuceConnectionFactory(configuration));
        return this;
    }

    public ClassOperationsImpl<H, HK, HV> opsForClass() {
        return opsForClass();
    }


}
