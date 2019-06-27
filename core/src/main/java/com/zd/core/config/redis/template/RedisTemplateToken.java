package com.zd.core.config.redis.template;

import com.zd.core.config.redis.RedisConfig;
import com.zd.core.redis.RedisTemplateClass;

public class RedisTemplateToken<H, HK, HV> extends RedisTemplateClass<H, HK, HV> {

    @Override
    public RedisTemplateToken factory(RedisConfig redisConfig) {
        super.factory(redisConfig);
        return this;
    }
}
