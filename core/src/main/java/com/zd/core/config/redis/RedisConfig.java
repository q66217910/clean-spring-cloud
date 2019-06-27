package com.zd.core.config.redis;

import lombok.Data;

@Data
public class RedisConfig {

    private String host;
    private Integer port;
    private String password;
    private Integer db;

}
