package com.zd.core.redis;

import com.zd.core.config.redis.RedisConfig;
import com.zd.core.redis.operations.LockOperations;
import com.zd.core.redis.operations.impl.ClassOperationsImpl;
import com.zd.core.redis.operations.impl.LockOperationsImpl;
import com.zd.core.redis.serializer.JacksonRedisSerializer;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.concurrent.locks.Lock;

public class RedisTemplateClass<HK, HV> extends RedisTemplate<HK, HV> {

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
        configuration.setPort(redisConfig.getPort());
        configuration.setHostName(redisConfig.getHost());
        LettuceConnectionFactory factory = new LettuceConnectionFactory(configuration);
        factory.afterPropertiesSet();
        this.setConnectionFactory(factory);
        this.afterPropertiesSet();
        return this;
    }

    public <H> ClassOperationsImpl<HK, H, HV> opsForClass() {
        return new ClassOperationsImpl<>(this);
    }

    public <H> LockOperations opsForLock() {
        return new LockOperationsImpl(this);
    }


}
