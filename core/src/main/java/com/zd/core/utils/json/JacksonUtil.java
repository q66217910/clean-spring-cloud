package com.zd.core.utils.json;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTypeResolverBuilder;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;

import java.io.IOException;

public class JacksonUtil {

    public static JacksonUtil jackson = new JacksonUtil().withCamel2Lower().withIgnoreUnknownPro();

    // jackson对象
    public static JacksonUtil jacksonCode = new JacksonUtil()
            .withNotNull()
            .withDisFailUnknown()
            .withIgnoreUnknown()
            .withBigAsPlain()
            .withAllowFieldNames()
            .withAllowControlChars()
            .withAllowSingleQuotes()
            .withDefualtTyping(new DefaultTypeResolverBuilder(DefaultTyping.NON_FINAL) {

                private static final long serialVersionUID = 1L;

                @Override
                public boolean useForType(JavaType javaType) {

                    // 这个默认值是什么类型的定义。
                    switch (this._appliesFor) {
                        // 涵盖的所有类型加上他们的所有数组类型。
                        case NON_CONCRETE_AND_ARRAYS:
                            while (javaType.isArrayType()) {
                                javaType = javaType.getContentType();
                            }
                            // 声明类型为{@link
                            // java.lang.Object}的属性或抽象类型（抽象类或接口）。
                        case OBJECT_AND_NON_CONCRETE:
                            return (javaType.getRawClass() == Object.class) || !javaType.isConcrete();
                        case NON_FINAL:
                            while (javaType.isArrayType()) {
                                javaType = javaType.getContentType();
                            }
                            if (javaType.getRawClass() == Long.class) {
                                return true;
                            }
                            return !javaType.isFinal();
                        default:
                            return (javaType.getRawClass() == Object.class);
                    }
                }

            });

    // 模式
    private ObjectMapper mapper;

    // 单例模式
    private ObjectMapper getMapper() {
        if (mapper == null) {
            synchronized (JacksonUtil.class) {
                if (mapper == null) {
                    mapper = new ObjectMapper();
                }
            }
        }
        return mapper;
    }

    /**
     * Float反序列化成BigDECIMAL
     */
    public JacksonUtil witDishBigForFloats() {
        getMapper().disable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        return this;
    }

    /**
     * 反序列化允许null
     */
    public JacksonUtil withDisAcceptNull() {
        getMapper().disable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
        return this;
    }

    /**
     * 反序列化允许null,String ""
     */
    public JacksonUtil withDisAcceptStringNull() {
        getMapper().disable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        return this;
    }

    /**
     * Float强转成int
     */
    public JacksonUtil withDisFloatAsInt() {
        getMapper().disable(DeserializationFeature.ACCEPT_FLOAT_AS_INT);
        return this;
    }

    /**
     * String[]强转成数组
     */
    public JacksonUtil withDisStringAsArray() {
        getMapper().disable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        return this;
    }

    /**
     * date转化成timeZone
     */
    public JacksonUtil withDisDataAsTimeZone() {
        getMapper().disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        return this;
    }

    public JacksonUtil withDisFETCH() {
        getMapper().disable(DeserializationFeature.EAGER_DESERIALIZER_FETCH);
        return this;
    }

    /**
     * 失败忽略
     */
    public JacksonUtil withDisFailIgnoged() {
        getMapper().disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
        return this;
    }

    /**
     * 未知属性
     */
    public JacksonUtil withDisFailUnknown() {
        getMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return this;
    }

    /**
     * 没定义的属性忽略
     */
    public JacksonUtil withIgnoreUnknown() {
        getMapper().enable(JsonGenerator.Feature.IGNORE_UNKNOWN);
        return this;
    }

    /**
     * 没定义的属性忽略
     */
    public JacksonUtil withIgnoreUnknownPro() {
        getMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return this;
    }

    /**
     * 用科学计数法表示
     */
    public JacksonUtil withBigAsPlain() {
        getMapper().enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
        return this;
    }

    /**
     *           *确定解析器是否允许使用的功能           *未引用的字段名称（由Javascript允许，          
     * *但不是JSON规范）。           *由于JSON规范要求使用双引号           *字段名称，          
     * *这是非标准功能，默认情况下禁用。          
     */
    public JacksonUtil withAllowFieldNames() {
        getMapper().enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        return this;
    }

    /**
     * 确定解析器是否允许的功能 JSON字符串包含无引号的控制字符（ASCII字符的值小于32，包括标签和换行字符）。
     * 如果将feature设置为false，则会抛出异常遇到字符由于JSON规范要求引用所有控制字符，这是非标准功能，默认情况下禁用。
     */
    public JacksonUtil withAllowControlChars() {
        getMapper().enable(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS);
        return this;
    }

    /**
     * 确定解析器是否允许使用的功能单引号（撇号，字符“\”）为引用字符串（名称和字符串值）。
     * 如果是这样，这是除了其他可接受的标记之外。但不是JSON规范）。 由于JSON规范要求使用双引号字段名称，这是非标准功能，默认情况下禁用。
     */
    public JacksonUtil withAllowSingleQuotes() {
        getMapper().enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        return this;
    }

    public JacksonUtil withDefualtTyping(TypeResolverBuilder<?> typeResolverBuilder) {
        typeResolverBuilder.init(JsonTypeInfo.Id.CLASS, null);
        typeResolverBuilder.inclusion(JsonTypeInfo.As.PROPERTY);
        getMapper().setDefaultTyping(typeResolverBuilder);
        return this;
    }

    /**
     * 伪值用于表示较高级别的默认值 意义上，避免超越包容性价值。 例如，如果返回           对于属性，这将使用包含的类的默认值
     * 财产，如有任何定义; 如果没有定义，那么 全局序列化包含细节。
     */
    public JacksonUtil withUseDefaults() {
        getMapper().setSerializationInclusion(Include.USE_DEFAULTS);
        return this;
    }

    /**
     * 表示所有的属性
     */
    public JacksonUtil withAll() {
        getMapper().setSerializationInclusion(Include.ALWAYS);
        return this;
    }

    /**
     * 表示只有具有值的属性
     */
    public JacksonUtil withNotEmpty() {
        getMapper().setSerializationInclusion(Include.NON_EMPTY);
        return this;
    }

    /**
     * 通常可以构建专门的文本对象的方法
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public JacksonUtil withOverrideAccess() {
        getMapper().getSerializationConfig().canOverrideAccessModifiers();
        return this;
    }

    /**
     * 访问者确定是否可以尝试强制重写访问
     */
    public JacksonUtil withCompileString(String src) {
        getMapper().getSerializationConfig().compileString(src);
        return this;
    }

    /**
     * 配置对象工厂
     */
    public JacksonUtil withConstruct() {
        getMapper().getSerializationConfig().constructDefaultPrettyPrinter();
        return this;
    }

    /**
     * 设置跟节点名称
     */
    public JacksonUtil withRootName(String rootName) {
        getMapper().getSerializationConfig().withRootName(rootName);
        return this;
    }

    /**
     * 表示仅属性为非空的值
     */
    public JacksonUtil withNotNull() {
        getMapper().setSerializationInclusion(Include.NON_NULL);
        return this;
    }

    /**
     * 是否缩放排列输出
     */
    public JacksonUtil withOrder() {
        getMapper().configure(SerializationFeature.INDENT_OUTPUT, true);
        return this;
    }

    /**
     * 是否环绕根元素(以类名作为根元素) 默认是true
     */
    public JacksonUtil withRoot() {
        getMapper().configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        return this;
    }

    /**
     * 将下划线转化成驼峰
     */
    public JacksonUtil withCamel2Lower() {
        getMapper().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        return this;
    }

    /**
     * 将首字母小写转化为大写
     */
    public JacksonUtil with2CamelCase() {
        getMapper().setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
        return this;
    }

    /**
     * 转化成全小写
     */
    public JacksonUtil with2Lower() {
        getMapper().setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CASE);
        return this;
    }

    /**
     * 序列化日期时以timestamps
     */
    public JacksonUtil withTimestamps() {
        getMapper().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        return this;
    }

    /**
     * 将枚举以String输出
     */
    public JacksonUtil withEnum2String() {
        getMapper().configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, false);
        return this;
    }

    /**
     * 将枚举以Ordinal输出
     */
    public JacksonUtil withEnum2Ordinal() {
        getMapper().configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, true);
        return this;
    }

    /**
     * 单个元素的数组不以数组输出
     */
    public JacksonUtil withArray() {
        getMapper().configure(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED, true);
        return this;
    }

    /**
     * 序列化Map时对key进行排序操作
     */
    public JacksonUtil withMapOrder() {
        getMapper().configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        return this;
    }

    /**
     * 序列化char[]时以json数组输出
     */
    public JacksonUtil withChar() {
        getMapper().configure(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS, true);
        return this;
    }

    /**
     * 将Object对象转化成json
     */
    public String obj2Json(Object obj) throws JsonProcessingException {
        if (obj == null) {
            return null;
        }
        return getMapper().writeValueAsString(obj);
    }

    /**
     * 将Object对象转化成byte数组
     */
    public byte[] obj2Byte(Object obj) throws JsonProcessingException {
        return getMapper().writeValueAsBytes(obj);
    }

    /**
     * 将json转化成Obj
     */
    public <T> T json2Obj(String json) throws JsonParseException, JsonMappingException, IOException {
        if (json == null) {
            return null;
        }
        return (T) getMapper().readValue(json, new TypeReference<Object>() {
        });
    }

    /**
     * 将byte数组转换成Obj
     */
    public <T> T byte2Obj(byte[] by) throws JsonParseException, JsonMappingException, IOException {
        return (T) getMapper().readValue(by, new TypeReference<Object>() {
        });
    }

    /**
     * 将json转化成bean对象
     */
    public <T> T json2Obj(String json, Class<T> t) throws JsonParseException, JsonMappingException, IOException {
        return getMapper().readValue(json, t);
    }

    public <T> T bean2Obj(Object obj) {
        try {
            return (T) getMapper().readValue(getMapper().writeValueAsString(obj), new TypeReference<Object>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将obj转换成对象
     */
    public <T> T obj2Bean(Object obj, Class<T> t)
            throws JsonParseException, JsonMappingException, JsonProcessingException, IOException {
        return getMapper().readValue(getMapper().writeValueAsString(obj), t);
    }

    /**
     * 将byte数组转换成对象
     */
    public <T> T byte2Bean(byte[] src, Class<T> t) throws JsonParseException, JsonMappingException, IOException {
        return getMapper().readValue(src, t);
    }

    public void printJson(Object obj) throws JsonProcessingException {
        System.out.println(obj2Json(obj));
    }

    public void printJson(String json) throws IOException {
        System.out.println(obj2Json(json2Obj(json)));
    }

    public void printByte(byte[] data) throws IOException {
        System.out.println(obj2Json(byte2Obj(data)));
    }

    /**
     * 清楚mapper
     */
    public void clear() {
        if (getMapper() != null) {
            synchronized (this) {
                if (getMapper() != null) {
                    this.mapper = null;
                }
            }
        }
    }

}
