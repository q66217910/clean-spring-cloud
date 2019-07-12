package com.zd.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("configuration")
@Data
public class ConfigurationProperty {

    /**
     * redis
     */
    private String[] redis = new String[0];

}
