package com.zd.core.utils.lambda;

import java.io.Serializable;
import java.util.function.Function;

@FunctionalInterface
public interface SerializableFunction<T, R> extends Function<T,R>, Serializable {
}
