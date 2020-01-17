package com.zd.feign.interceptor;

import feign.RequestTemplate;
import feign.auth.BasicAuthRequestInterceptor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class TokenInterceptor extends BasicAuthRequestInterceptor {

    public TokenInterceptor() {
        super("", "");
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            String authorization = request.getHeader("Authorization");
            if (Strings.isNotEmpty(authorization)) {
                requestTemplate.header("Authorization", authorization);
                return;
            }
        }
        super.apply(requestTemplate);
    }

}
