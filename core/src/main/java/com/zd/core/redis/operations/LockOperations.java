package com.zd.core.redis.operations;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public interface LockOperations extends LockAsync {


    /**
     * 尝试获取锁
     */
    boolean tryLock(String keyName, long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException, ExecutionException;

    /**
     * @param keyName   锁名
     * @param leaseTime 时间
     * @param unit      时间单位
     * @throws InterruptedException
     */
    void lockInterruptibly(String keyName, long leaseTime, TimeUnit unit) throws InterruptedException;

}
