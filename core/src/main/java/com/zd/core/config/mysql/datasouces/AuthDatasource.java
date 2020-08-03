package com.zd.core.config.mysql.datasouces;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;


/**
 * 授权数据库
 */
@Configuration
public class AuthDatasource {

    @Bean(name="authDatasource")
    @Primary
    @ConfigurationProperties(prefix = "mysql.auth")
    public DataSource authDatasource(){
        return DataSourceBuilder.create().build();
    }

}
