package com.zd.core.config.redis.template;

import com.zd.core.config.redis.RedisConfig;
import com.zd.core.redis.RedisTemplateClass;

public class RedisTemplateUser<H, HV> extends RedisTemplateClass<H, HV> {

    @Override
    public RedisTemplateUser factory(RedisConfig redisConfig) {
        super.factory(redisConfig);
        return this;
    }
}
