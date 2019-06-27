package com.zd.core.redis.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zd.core.utils.json.JacksonUtil;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.IOException;

/**
 * redis Jackson 序列化
 */
public class JacksonRedisSerializer<T> implements RedisSerializer<T> {

    @Override
    public byte[] serialize(T t) throws SerializationException {
        try {
            return JacksonUtil.jacksonCode.obj2Byte(t);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        try {
            return JacksonUtil.jacksonCode.byte2Obj(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
