package com.zd.feign.fallback;

import feign.hystrix.FallbackFactory;

public class MyFallbackFactory implements FallbackFactory {
    
    @Override
    public Object create(Throwable cause) {
        return null;
    }
}
