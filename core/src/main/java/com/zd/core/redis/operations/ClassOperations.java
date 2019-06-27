package com.zd.core.redis.operations;

import com.zd.core.utils.lambda.FunctionCollection;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * class 操作
 */
public interface ClassOperations<H, HK, HV> {

    /**
     * @param values 对象信息
     * @return 存储redis 主键:属性  值的形式
     */
    default void putClass(HV... values) {
         putClass(Arrays.asList(values));
    }

    ;

    /**
     * @param values 对象信息
     * @return 存储redis 主键:属性  值的形式
     */
    default void putClass(Collection<HV> values) {
         putClass(null, values);
    }

    ;

    /**
     * @param key    redis key
     * @param values 对象信息
     * @return 存储redis 主键:属性  值的形式
     */
    default void putClass(H key, HV... values) {
         putClass(key, Arrays.asList(values));
    };

    /**
     * @param key    redis key
     * @param values 对象信息
     * @return 存储redis 主键:属性  值的形式
     */
    void putClass(H key, Collection<HV> values);


    /**
     * @param key     redis key
     * @param hashKey 主键值
     * @return 获取对象（适用于单个主键）
     */
    HV getClass(H key, FunctionCollection<HV> columns, Object hashKey);

    /**
     * @param hashKey 主键值
     * @return 获取对象（适用于单个主键）
     */
    default HV getClass(HV hashKey) {
        return getClass(null, FunctionCollection.create(), hashKey);
    }

    /**
     * @param hashKey 主键值
     * @return 获取对象（适用于单个主键）
     */
    default HV getClass(HV hashKey, FunctionCollection<HV> columns) {
        return getClass(null, columns, hashKey);
    }

    /**
     * @param key     redis key
     * @param hashKey 主键值
     * @return 获取对象（适用于单个主键）
     */
    default HV getClass(H key, Object hashKey) {
        return getClass(key, FunctionCollection.create(), hashKey);
    }

    /**
     * @param key     redis key
     * @param hashKey 主键值
     * @return 获取对象（适用于单个主键）
     */
    List<HV> getClass(H key, FunctionCollection<HV> columns, Collection<HV> hashKey);

    /**
     * @param key     redis key
     * @param hashKey 主键值
     * @return 获取对象（适用于单个主键）
     */
    default List<HV> getClass(H key, Collection<HV> hashKey) {
        return getClass(key, FunctionCollection.create(), hashKey);
    }

    /**
     * @param hashKey 主键值
     * @return 获取对象（适用于单个主键）
     */
    default List<HV> getClass(Collection<HV> hashKey) {
        return getClass(null, FunctionCollection.create(), hashKey);
    }

}
