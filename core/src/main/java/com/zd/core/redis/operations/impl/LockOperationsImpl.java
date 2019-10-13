package com.zd.core.redis.operations.impl;

import com.zd.core.redis.operations.LockOperations;
import io.netty.util.Timeout;
import lombok.Data;
import lombok.NonNull;
import org.redisson.RedissonLock;
import org.redisson.client.protocol.RedisCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * reids 分布式锁
 */
@Data
public class LockOperationsImpl implements LockOperations {

    private final @NonNull RedisTemplate redisTemplate;

    private static final ConcurrentMap<String, RedissonLock.ExpirationEntry> EXPIRATION_RENEWAL_MAP = new ConcurrentHashMap<>();

    private long lockWatchdogTimeout;

    private String id;

    private String entryName;

    protected String getLockName(long threadId) {
        return id + ":" + threadId;
    }
    
    @Override
    public boolean tryLock(String keyName, long waitTime, long leaseTime, TimeUnit unit) throws ExecutionException, InterruptedException {
        return tryLockAsync(keyName).get();
    }

    @Override
    public Future<Boolean> tryLockAsync(String keyName) {
        return tryLockAsync(keyName, Thread.currentThread().getId());
    }

    @Override
    public Future<Boolean> tryLockAsync(String keyName, long threadId) {
        return tryAcquireOnceAsync(keyName, -1, null, threadId);
    }

    private Future<Boolean> tryAcquireOnceAsync(String keyName, long leaseTime, TimeUnit unit, long threadId) {
        if (leaseTime != -1) {
            return tryLockInnerAsync(keyName, leaseTime, unit, threadId);
        }
        CompletableFuture<Boolean> ttlRemainingFuture = tryLockInnerAsync(keyName, lockWatchdogTimeout, TimeUnit.MILLISECONDS, threadId);
        ttlRemainingFuture.thenAccept(res -> {
            if (res) {
                scheduleExpirationRenewal(threadId);
            }
        });
        return ttlRemainingFuture;
    }

    private void scheduleExpirationRenewal(long threadId) {
        RedissonLock.ExpirationEntry entry = new RedissonLock.ExpirationEntry();
        RedissonLock.ExpirationEntry oldEntry = EXPIRATION_RENEWAL_MAP.putIfAbsent(getEntryName(), entry);
        if (oldEntry != null) {
            oldEntry.addThreadId(threadId);
        } else {
            entry.addThreadId(threadId);
            renewExpiration();
        }
    }

    private void renewExpiration() {
        RedissonLock.ExpirationEntry ee = EXPIRATION_RENEWAL_MAP.get(getEntryName());
        if (ee == null) {
            return;
        }
    }


    @SuppressWarnings("unchecked")
    <T> CompletableFuture<T> tryLockInnerAsync(String keyName, long leaseTime, TimeUnit unit, long threadId) {
        long internalLockLeaseTime = unit.toMillis(leaseTime);
        return CompletableFuture.supplyAsync(() ->
                (T) redisTemplate.execute(RedisScript.<T>of("if (redis.call('exists', KEYS[1]) == 0) then " +
                        "redis.call('hset', KEYS[1], ARGV[2], 1); " +
                        "redis.call('pexpire', KEYS[1], ARGV[1]); " +
                        "return nil; " +
                        "end; " +
                        "if (redis.call('hexists', KEYS[1], ARGV[2]) == 1) then " +
                        "redis.call('hincrby', KEYS[1], ARGV[2], 1); " +
                        "redis.call('pexpire', KEYS[1], ARGV[1]); " +
                        "return nil; " +
                        "end; " +
                        "return redis.call('pttl', KEYS[1]);"), Collections.<Object>singletonList(keyName), internalLockLeaseTime, getLockName(threadId))
        );

    }

    @Override
    public void lockInterruptibly(String keyName, long leaseTime, TimeUnit unit) throws InterruptedException {

    }

    public static class ExpirationEntry {

        private final Map<Long, Integer> threadIds = new LinkedHashMap<>();
        private volatile Timeout timeout;

        public ExpirationEntry() {
            super();
        }

        public void addThreadId(long threadId) {
            Integer counter = threadIds.get(threadId);
            if (counter == null) {
                counter = 1;
            } else {
                counter++;
            }
            threadIds.put(threadId, counter);
        }

        public boolean hasNoThreads() {
            return threadIds.isEmpty();
        }

        public Long getFirstThreadId() {
            if (threadIds.isEmpty()) {
                return null;
            }
            return threadIds.keySet().iterator().next();
        }

        public void removeThreadId(long threadId) {
            Integer counter = threadIds.get(threadId);
            if (counter == null) {
                return;
            }
            counter--;
            if (counter == 0) {
                threadIds.remove(threadId);
            } else {
                threadIds.put(threadId, counter);
            }
        }


        public void setTimeout(Timeout timeout) {
            this.timeout = timeout;
        }

        public Timeout getTimeout() {
            return timeout;
        }
    }
}
