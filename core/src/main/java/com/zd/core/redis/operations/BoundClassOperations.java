package com.zd.core.redis.operations;

import org.springframework.data.redis.core.BoundKeyOperations;

/**
 * class 操作
 */
public interface BoundClassOperations<H, HK, HV> extends BoundKeyOperations<H> {
}
