package com.zd.zuul.fallback.route;

import com.zd.zuul.fallback.CommonFallbackProvider;
import org.springframework.stereotype.Component;

@Component
public class AuthorizedFallbackProvider extends CommonFallbackProvider {

    @Override
    public String getRoute() {
        return "authorized";
    }
    
}
