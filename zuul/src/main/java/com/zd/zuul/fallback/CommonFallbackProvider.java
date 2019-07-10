package com.zd.zuul.fallback;

import com.zd.core.bean.ResultBean;
import com.zd.core.utils.json.JacksonUtil;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@SuppressWarnings("NullableProblems")
public abstract class CommonFallbackProvider implements FallbackProvider {

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() {
                //获取状态码(200,OK)
                return HttpStatus.OK;
            }

            @Override
            public int getRawStatusCode() {
                //返回数字状态码
                return 200;
            }

            @Override
            public String getStatusText() {
                //返回字母状态码
                return "OK";
            }

            @Override
            public void close() {

            }

            @Override
            public InputStream getBody() throws IOException {
                //返回的内容
                return new ByteArrayInputStream(JacksonUtil.jackson.obj2Byte(ResultBean.builder().code("500").msg("熔断").build()));
            }

            @Override
            public HttpHeaders getHeaders() {
                //返回时的Header体的设置
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return headers;
            }
        };
    }
}
