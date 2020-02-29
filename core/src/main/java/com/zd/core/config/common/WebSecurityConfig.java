package com.zd.core.config.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private WebApplicationContext applicationContext;

    /**
     * http安全配置
     *
     * @param http http安全对象
     * @throws Exception http安全异常信息
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        String[] urls = mapping.getHandlerMethods()
                .keySet()
                .stream()
                .map(RequestMappingInfo::getPatternsCondition)
                .map(PatternsRequestCondition::getPatterns)
                .map(a->a.stream().findAny().get())
                .toArray(String[]::new);
        http.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers("/favicon.ico", "/webjars/**", "/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html").permitAll()
                .and().httpBasic().and().csrf().disable();
    }
}
