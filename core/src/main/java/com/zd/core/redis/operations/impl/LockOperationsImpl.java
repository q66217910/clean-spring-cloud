package com.zd.core.redis.operations.impl;

import com.zd.core.redis.operations.LockOperations;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.Data;
import lombok.NonNull;
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

    private static final ConcurrentMap<String, LockOperationsImpl.ExpirationEntry> EXPIRATION_RENEWAL_MAP = new ConcurrentHashMap<>();

    private long lockWatchdogTimeout;

    private String id;

    private String entryName;

    protected long internalLockLeaseTime;

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
                scheduleExpirationRenewal(keyName, threadId);
            }
        });
        return ttlRemainingFuture;
    }

    private void scheduleExpirationRenewal(String keyName, long threadId) {
        LockOperationsImpl.ExpirationEntry entry = new LockOperationsImpl.ExpirationEntry();
        LockOperationsImpl.ExpirationEntry oldEntry = EXPIRATION_RENEWAL_MAP.putIfAbsent(getEntryName(), entry);
        if (oldEntry != null) {
            oldEntry.addThreadId(threadId);
        } else {
            entry.addThreadId(threadId);
            renewExpiration(keyName);
        }
    }

    private void renewExpiration(String keyName) {
        LockOperationsImpl.ExpirationEntry ee = EXPIRATION_RENEWAL_MAP.get(getEntryName());
        if (ee == null) {
            return;
        }
        Timeout task = new HashedWheelTimer().newTimeout(timeout -> {
            ExpirationEntry ent = EXPIRATION_RENEWAL_MAP.get(getEntryName());
            if (ent == null) {
                return;
            }
            Long threadId = ent.getFirstThreadId();
            if (threadId == null) {
                return;
            }
            CompletableFuture<Boolean> future = renewExpirationAsync(keyName, threadId);
            future.thenAccept(res -> {
                if (res) {
                    // reschedule itself
                    renewExpiration(keyName);
                }
            });
        }, internalLockLeaseTime / 3, TimeUnit.MILLISECONDS);

        ee.setTimeout(task);
    }

    protected CompletableFuture<Boolean> renewExpirationAsync(String keyName, long threadId) {
        return CompletableFuture.supplyAsync(() ->
                (Boolean) redisTemplate.execute(RedisScript.<Boolean>of(
                        "if (redis.call('hexists', KEYS[1], ARGV[2]) == 1) then " +
                                "redis.call('pexpire', KEYS[1], ARGV[1]); " +
                                "return 1; " +
                                "end; " +
                                "return 0;"), Collections.<Object>singletonList(keyName), internalLockLeaseTime, getLockName(threadId))
        );
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
