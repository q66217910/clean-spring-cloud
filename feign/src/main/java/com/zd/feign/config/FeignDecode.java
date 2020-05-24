package com.zd.feign.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.zd.core.bean.ResultBean;
import com.zd.core.exception.BusinessException;
import feign.Retryer;
import feign.Util;
import feign.codec.Decoder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
        this.mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
    }

    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default();
    }

    @Bean
    public Decoder feignDecoder() {
        return (response, type) -> {
            boolean isResult = isResultBean(type);
            byte[] rowBytes = Util.toByteArray(response.body().asInputStream());
            ResultBean resultBean = null;
            try {
                resultBean = mapper.readValue(rowBytes, buildJavaType(type, isResult));
            } catch (Exception e) {
                // 序列化错误
                return response.body();
            }
            if (resultBean.getCode().equals("0")) {
                //返回值带ResultBean
                if (isResult) {
                    return resultBean;
                }
                return resultBean.getData();
            } else {
                throw new BusinessException(resultBean.getMsg());
            }
        };
    }

    private boolean isResultBean(Type type) {
        Class firstClass;
        if (type instanceof ParameterizedType) {
            // 获取泛型类型
            firstClass = (Class) ((ParameterizedType) type).getRawType();

        } else {
            firstClass = (Class) type;
        }
        return ResultBean.class.isAssignableFrom(firstClass);
    }

    private JavaType buildJavaType(Type type, boolean isResult) {
        JavaType javaType = getJavaType(type);
        if (isResult) {
            return javaType;
        }
        // 构建ApiResponses类型
        return _typeFactory.constructParametricType(ResultBean.class, javaType);
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
            return _typeFactory.constructType(type);
        }
    }
}
