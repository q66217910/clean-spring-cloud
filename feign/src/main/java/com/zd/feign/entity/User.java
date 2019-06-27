package com.zd.feign.entity;

import com.zd.core.redis.annotation.RedisKey;
import com.zd.core.redis.annotation.RedisPrimaryKey;
import lombok.Data;

@Data
@RedisKey("user:detail")
public class User {

    @RedisPrimaryKey
    private Integer userId;

    private String userName;
}
