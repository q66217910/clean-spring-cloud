package com.zd.core.redis.operations;

import java.util.concurrent.Future;

public interface LockAsync {


    Future<Boolean> tryLockAsync(String keyName);

    Future<Boolean> tryLockAsync(String keyName, long threadId);
}
