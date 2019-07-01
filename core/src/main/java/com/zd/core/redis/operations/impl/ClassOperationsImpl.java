package com.zd.core.redis.operations.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zd.core.redis.annotation.RedisKey;
import com.zd.core.redis.annotation.RedisPrimaryKey;
import com.zd.core.redis.operations.ClassOperations;
import com.zd.core.utils.lambda.FunctionCollection;
import com.zd.core.utils.lambda.LambdaUtils;
import com.zd.core.utils.type.Convert;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@SuppressWarnings("unchecked")
public class ClassOperationsImpl<H, HK, HV> implements ClassOperations<H, HK, HV> {

    private final @NonNull RedisTemplate<H, HV> redisTemplate;

    private final static String CLASS = "@class";

    private static String DIVIDE = ":";

    @Override
    public void putClass(H key, Collection<HV> values) {
        Map<String, Object> hashKey = getHashKey(values);
        redisTemplate.opsForHash().putAll(getKey(key,values.stream().findFirst().get().getClass()), hashKey);
    }


    @Override
    public HV getClass(H key, FunctionCollection<HV> columns, Object hashKey) {
        Assert.notNull(hashKey, "hashKey must not be null!");
        List<String> fields = LambdaUtils.columnsToString(columns.getFunctions());
        try {
            HV hv = (HV) hashKey;
            return this.getClassDetail(getKey(key, hv.getClass()), Lists.newArrayList(hashKey), (Class<HV>) hv.getClass(), fields).stream().findFirst().get();
        } catch (ClassCastException e) {
            //强转错误
        }
        Assert.notNull(key, "key must not be null!");
        return this.getClassDetail(key, Lists.newArrayList(hashKey), null, fields).stream().findFirst().get();
    }


    @Override
    public List<HV> getClass(H key, FunctionCollection<HV> columns, Collection<HV> hashKey) {
        Assert.notNull(key, "key must not be null!");
        Assert.isTrue(hashKey != null && hashKey.size() > 0, "hashKey must not be null!");
        List<String> fields = LambdaUtils.columnsToString(columns.getFunctions());
        return this.getClassDetail(key, hashKey, (Class<HV>) hashKey.toArray()[0].getClass(), fields);
    }

    private List<HV> getClassDetail(H key, Collection hashKey, Class<HV> clazz, List<String> fields) {
        clazz = this.getClassByName(key, clazz);
        boolean ret = clazz != null;
        List<String> keys = Lists.newArrayList();
        Map<Object, String> preKey = Maps.newHashMap();
        ClassField<HV> classField = getClassField(clazz, fields, field -> keys.addAll(findHashKey(field, hashKey, preKey, ret)));
        if (!ret) {
            Assert.isTrue(classField.getPrimaryKey().size() == 1, "该方法只适用于单个主键");
        }
        BoundHashOperations<H, String, Object> hash = this.redisTemplate.boundHashOps(key);
        List<Object> values = hash.multiGet(keys);
        if (values == null) {
            values = Lists.newArrayList();
        }
        Map<String, Map<String, Object>> map = toMap(keys, values);
        return doFinally(classField, map);
    }

    private H getKey(H key, Class<?> clazz) {
        if (key == null) {
            Assert.notNull(clazz, "clazz must not be null!");
            RedisKey redisKey = clazz.getAnnotation(RedisKey.class);
            Assert.notNull(clazz, "clazz must not be null!");
            key = (H) redisKey.value();
            Assert.notNull(key, "@RedisKey value 不能没有key");
        }
        return key;
    }

    /**
     * @param keys   redis Hash Key
     * @param values redis Hash Value
     * @return 组成 FieldName-value Map
     */
    private Map<String, Map<String, Object>> toMap(List<String> keys, List<Object> values) {
        Assert.isTrue(keys.size() == values.size(), "key和value数量不相等 ");
        Map<String, Map<String, Object>> allMap = Maps.newHashMap();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            Object value = values.get(i);
            String[] split = key.split(":");
            Assert.isTrue(split.length > 0, "分割属性出错 ");
            String hashKey = key.substring(0, key.lastIndexOf(":"));
            Map<String, Object> map = allMap.get(hashKey);
            if (map == null) {
                map = Maps.newHashMap();
            }
            String fieldName = split[split.length - 1];
            map.put(fieldName, value);
            allMap.put(hashKey, map);
        }
        return allMap;
    }

    /**
     * @return map 转成属性对象
     */
    private List<HV> doFinally(ClassField<HV> classField, Map<String, Map<String, Object>> valueMap) {
        List<HV> hvs = Lists.newArrayList();
        try {
            for (Map<String, Object> entry : valueMap.values()) {
                HV hv = classField.getClazz().getDeclaredConstructor().newInstance();
                for (Field field : classField.getDeclaredFields()) {
                    field.setAccessible(true);
                    field.set(hv, entry.get(field.getName()));
                }
                hvs.add(hv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hvs;
    }

    /**
     * @param field   字段
     * @param hashKey 传入的值
     * @param preKey  前缀
     * @param ret     是否为对象
     * @return hashkey
     */
    private List<String> findHashKey(Field field, Collection hashKey, Map<Object, String> preKey, boolean ret) {
        List<String> list = Lists.newArrayList();
        for (Object o : hashKey) {
            if (ret) {
                try {
                    if (!preKey.containsKey(hashKey)) {
                        String hashKeyPre = getHashKeyPre((HV) o);
                        preKey.put(o, hashKeyPre);
                        list.add(new StringJoiner(DIVIDE).add(hashKeyPre).add(field.getName()).toString());
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                list.add(new StringJoiner(DIVIDE).add(Objects.toString(hashKey)).add(field.getName()).toString());
            }
        }
        return list;
    }

    private <R> ClassField<HV> getClassField(Class<HV> clazz, List<String> fields, Function<Field, R> function) {
        List<String> primaryKey = Lists.newArrayList();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.getAnnotation(RedisPrimaryKey.class) != null) {
                primaryKey.add(field.getName());
            }
            if (fields.size() > 0 && !fields.contains(field.getName())) {
                continue;
            }
            field.setAccessible(true);
            function.apply(field);
        }
        return new ClassField<>(primaryKey, declaredFields, clazz);
    }

    /**
     * @param key   redis key
     * @param clazz 类型
     * @return 根据名称获取类型
     */
    private Class<HV> getClassByName(H key, Class<HV> clazz) {
        if (clazz == null) {
            try {
                String className = Convert.toString(this.redisTemplate.boundHashOps(key).get(CLASS));
                clazz = (Class<HV>) Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException("clazz 不存在");
            }
        }
        return clazz;
    }

    private Map<String, Object> getHashKey(Collection<HV> values) {
        Map<String, Object> map = Maps.newHashMap();
        Class<?> clazz = null;
        try {
            Field[] declaredFields = null;
            for (HV hv : values) {
                if (clazz == null) {
                    clazz = hv.getClass();
                    declaredFields = clazz.getDeclaredFields();
                }
                String hash = getHashKeyPre(hv);
                for (Field field : declaredFields) {
                    field.setAccessible(true);
                    map.put(hash + ":" + field.getName(), field.get(hv));
                }
                map.put(CLASS, clazz.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 获取 hashkey 前缀
     */
    private String getHashKeyPre(HV value) throws IllegalAccessException {
        Map<String, Object> keyMap = Maps.newHashMap();
        Class<?> clazz = value.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.getAnnotation(RedisPrimaryKey.class) != null) {
                field.setAccessible(true);
                Object fieldValue = field.get(value);
                Assert.notNull(fieldValue, "主键值不能为空");
                keyMap.put(field.getName(), fieldValue);
            }
        }
        Assert.notEmpty(keyMap, "Redis 对象不能没有 @RedisPrimaryKey 主键 ");
        return keyMap.entrySet()
                .stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .map(Map.Entry::getValue)
                .map(Object::toString)
                .collect(Collectors.joining(":"));

    }

    /**
     * class存储对象
     */
    @Data
    class ClassField<HV> {

        private List<String> primaryKey;//主键列表

        private Field[] declaredFields;//属性数组

        private List<String> keys;//REDIS HASH KEY

        private Class<HV> clazz;

        public ClassField(List<String> primaryKey, Field[] declaredFields, Class<HV> clazz) {
            this.primaryKey = primaryKey;
            this.declaredFields = declaredFields;
            this.clazz = clazz;
        }
    }
}
