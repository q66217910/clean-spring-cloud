package com.zd.core.redis.operations;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.listener.*;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class RedisLocalCache {

    private final @NonNull RedisTemplate<String, ?> redisTemplate;

    //总集合
    private static Map<String, Object> cacheMap = new ConcurrentHashMap<>();

    private static int LFU_VALUE = 100;

    /**
     * 获取某个key的LFU值
     *
     * @param key redis key
     * @return LFU值
     */
    private int getLfuValue(String key) {
        return Optional.ofNullable(redisTemplate.execute(
                RedisScript.<Integer>of("return redis.call('OBJECT','freq',KEYS[1])"),
                Lists.newArrayList(key)))
                .orElse(0);
    }

    /**
     * 过期通知处理
     */
    @Component
    private static class expiredHandler extends KeyExpirationEventMessageListener {

        @Override
        protected void doHandleMessage(Message message) {
            cacheMap.remove(message.toString());
        }

        public expiredHandler(RedisMessageListenerContainer listenerContainer) {
            super(listenerContainer);
        }
    }

    /**
     * 过期通知处理
     */
    @Component
    private static class hashHandler extends KeyspaceEventMessageListener {


        public hashHandler(RedisMessageListenerContainer listenerContainer) {
            super(listenerContainer);
        }
        
        @Override
        protected void doHandleMessage(Message message) {

        }
    }
}
