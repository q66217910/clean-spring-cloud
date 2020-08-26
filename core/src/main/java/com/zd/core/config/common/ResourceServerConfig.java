package com.zd.core.config.common;

import com.zd.core.bean.ResultBean;
import com.zd.core.config.common.properties.Oauth2Property;
import com.zd.core.utils.json.JacksonUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final Oauth2Property oauth2Property;

    private final ApplicationContext context;

    public ResourceServerConfig(Oauth2Property oauth2Property, ApplicationContext context) {
        this.oauth2Property = oauth2Property;
        this.context = context;
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.authenticationEntryPoint((request, response, accessDeniedException) -> {
            //401
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            try (ServletOutputStream os = response.getOutputStream()) {
                os.write(JacksonUtil.jackson.obj2Byte(ResultBean.unauthorized()));
            }
        }).accessDeniedHandler((request, response, accessDeniedException) -> {
            //403
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            try (ServletOutputStream os = response.getOutputStream()) {
                os.write(JacksonUtil.jackson.obj2Byte(ResultBean.forbidden()));
            }
        });
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        RequestMappingHandlerMapping mapping = context.getBean(RequestMappingHandlerMapping.class);
        String[] urls = mapping.getHandlerMethods()
                .keySet()
                .stream()
                .map(RequestMappingInfo::getPatternsCondition)
                .map(PatternsRequestCondition::getPatterns)
                .flatMap(Collection::stream)
                .toArray(String[]::new);
        http.requestMatcher(new OAuth2RequestedMatcher())
                .authorizeRequests()
                .antMatchers("/oauth/**").authenticated()
                .antMatchers("/favicon.ico", "/webjars/**", "/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html").permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers(urls).authenticated()
                .and().formLogin().permitAll()
                .and().httpBasic().disable();
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
