package com.zd.core.utils.lambda;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 参数方法集合
 */
@Data
@AllArgsConstructor
public class FunctionCollection<T> {

    private List<SerializableFunction<T, ?>> functions;

    public static <T> FunctionCollection<T> create() {
        return new FunctionCollection<>(Lists.newArrayList());
    }

    public FunctionCollection<T> add(SerializableFunction<T, ?> function) {
        functions.add(function);
        return this;
    }
}
