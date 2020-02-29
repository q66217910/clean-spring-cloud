package com.zd.core.config.common;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("通用api")
                .build();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.zd"))
                .build()
                .securitySchemes(Collections.singletonList(oauth()))
                .enable(true);
    }

    @Bean
    SecurityScheme oauth() {
        return new OAuthBuilder()
                .name("OAuth2")
                .scopes(scopes())
                .grantTypes(grantTypes())
                .build();
    }

    @Bean
    List<GrantType> grantTypes() {
        return Collections.singletonList(new ClientCredentialsGrant("/oauth/token"));
    }

    private List<AuthorizationScope> scopes() {
        return Arrays.asList(new AuthorizationScope("read", "Grants read access"),
                new AuthorizationScope("write", "Grants write access"),
                new AuthorizationScope("trust", "Grants read write and delete access"));
    }

}
