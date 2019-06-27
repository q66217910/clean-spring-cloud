package com.zd.core.redis.annotation;

import java.lang.annotation.*;

/**
 * redis 懒加载
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisKey {

    /**
     * @return 使用的redisTemplate
     */
    String redisTemplate() default "redisTemplateRule";

    /**
     * @return redis存储key
     */
    String value();

}
