package com.zd.core.redis;

import com.zd.core.redis.operations.impl.ClassOperationsImpl;
import com.zd.core.redis.serializer.JacksonRedisSerializer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

public class RedisTemplateClass<H, HK, HV> extends RedisTemplate<HK, HV> {

    public RedisTemplateClass() {
        this.setKeySerializer(RedisSerializer.string());
        this.setHashKeySerializer(RedisSerializer.string());
        this.setValueSerializer(new JacksonRedisSerializer<>());
        this.setHashValueSerializer(new JacksonRedisSerializer<>());
    }

    public ClassOperationsImpl<H, HK, HV> opsForClass() {
        return opsForClass();
    }


}
