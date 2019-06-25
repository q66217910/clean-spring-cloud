package com.zd.user.config;

import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;

public class ClientDetailsConfig extends ClientDetailsServiceConfigurer {

    public ClientDetailsConfig(ClientDetailsServiceBuilder<?> builder) {
        super(builder);
    }

    @Override
    public void configure(ClientDetailsServiceBuilder<?> builder) throws Exception {
        builder.inMemory()
                .withClient("first").secret("passwordforauthserver")
                .redirectUris("http://localhost:8080/").authorizedGrantTypes("authorization_code", "refresh_token")
                .scopes("myscope").autoApprove(true).accessTokenValiditySeconds(30).refreshTokenValiditySeconds(1800)
                .and()
                .withClient("second").secret("passwordforauthserver")
                .redirectUris("http://localhost:8081/").authorizedGrantTypes("authorization_code", "refresh_token")
                .scopes("myscope").autoApprove(true).accessTokenValiditySeconds(30).refreshTokenValiditySeconds(1800);

    }
}
