package com.zd.geteway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

@Configuration
public class LoginRouteConfig {

    @Bean
    public RouteLocator loginRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/login").uri("localhost:23001"))
                .build();
    }

    @Bean
    public OAuth2RestTemplate loadBalancedOauth2RestTemplate(OAuth2ProtectedResourceDetails resource) {
        ClientCredentialsResourceDetails detail = new ClientCredentialsResourceDetails();
        detail.setClientId(resource.getClientId());
        detail.setClientSecret(resource.getClientSecret());
        detail.setAccessTokenUri(resource.getAccessTokenUri());
        return new OAuth2RestTemplate(detail, new DefaultOAuth2ClientContext());
    }
}
