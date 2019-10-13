package com.zd.core.redis.operations.impl;

import org.redisson.api.RFuture;

import java.util.concurrent.Future;

public interface LockAsync {


    Future<Boolean> tryLockAsync(String keyName);

    Future<Boolean> tryLockAsync(String keyName, long threadId);
}
