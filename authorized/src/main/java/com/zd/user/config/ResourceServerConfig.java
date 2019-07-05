package com.zd.user.config;

import com.zd.user.config.properties.Oauth2Property;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final Oauth2Property oauth2Property;

    public ResourceServerConfig(Oauth2Property oauth2Property) {
        this.oauth2Property = oauth2Property;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatcher(new OAuth2RequestedMatcher())
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .anyRequest().authenticated();
    }

    /**
     * 定义OAuth2请求匹配器
     */
    private class OAuth2RequestedMatcher implements RequestMatcher {
        @Override
        public boolean matches(HttpServletRequest request) {
            String auth = request.getHeader(oauth2Property.getHeader());
            //判断来源请求是否包含oauth2授权信息,这里授权信息来源可能是头部的Authorization值以Bearer开头
            return (auth != null) && auth.startsWith(oauth2Property.getStart());
        }
    }
}