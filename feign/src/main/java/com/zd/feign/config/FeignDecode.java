package com.zd.feign.config;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.zd.core.bean.ResultBean;
import com.zd.core.exception.BusinessException;
import feign.Util;
import feign.codec.Decoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Configuration
public class FeignDecode {

    private final TypeFactory _typeFactory = TypeFactory.defaultInstance();

    private final ObjectMapper mapper;

    public FeignDecode() {
        this.mapper = new ObjectMapper();
    }

    @Bean
    public Decoder feignDecoder() {
        return (response, type) -> {
            byte[] rowBytes = Util.toByteArray(response.body().asInputStream());
            ResultBean resultBean = null;
            try {
                resultBean = mapper.readValue(rowBytes, buildJavaType(type));
            } catch (Exception e) {
                // 序列化错误
                return response.body();
            }
            if (resultBean.getCode().equals("0")) {
                return resultBean.getData();
            } else {
                throw new BusinessException(resultBean.getMsg());
            }
        };
    }

    private JavaType buildJavaType(Type type) {
        // 构建ApiResponses类型
        return _typeFactory.constructParametricType(ResultBean.class, getJavaType(type));
    }

    private JavaType getJavaType(Type type) {
        // 判断是否带泛型的类型
        if (type instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();

            JavaType[] javaTypes = new JavaType[actualTypeArguments.length];

            for (int i = 0; i < actualTypeArguments.length; i++) {
                //泛型也可能带有泛型，递归获取
                javaTypes[i] = getJavaType(actualTypeArguments[i]);
            }

            // 获取泛型类型
            Class rowClass = (Class) ((ParameterizedType) type).getRawType();

            return _typeFactory.constructParametricType(rowClass, javaTypes);
        } else {
            // 简单类型直接用该类构建JavaType
            return _typeFactory.constructParametricType((Class) type, new JavaType[0]);
        }
    }
}
