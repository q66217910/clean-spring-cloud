package com.zd.core.config.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("token.oauth2")
@Data
public class Oauth2Property {

    /**
     * token存储的头部
     */
    private String header;

    /**
     * token的开始信息
     */
    private String start;

}
