package com.zd.core.utils.type;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author ZD
 */
public class Convert {

    /**
     * false字符串
     */
    private static String FALSE = "false";


    @SuppressWarnings("unchecked")
    public static <T> T toObj(Object obj, T t) {
        return obj == null ? t : (T) obj;
    }

    public static long toLong(Object obj, long def) {
        return obj == null ? def : Long.valueOf(String.valueOf(obj));
    }

    public static long toLong(Object obj) {
        return toLong(obj, 0L);
    }

    public static int toInt(Object obj) {
        return toInt(obj, 0);
    }

    public static int toInt(Object obj, int def) {
        return obj == null ? def : Integer.valueOf(String.valueOf(obj));
    }

    public static double toDouble(Object obj) {
        return toDouble(obj, 0.0);
    }

    public static double toDouble(Object obj, double def) {
        return obj == null ? def : Double.valueOf(String.valueOf(obj));
    }

    public static String toString(Object obj) {
        return toString(obj, "");
    }

    public static String toString(Object obj, String def) {
        return obj == null ? def : String.valueOf(obj);
    }

    public static boolean toBool(Object obj) {
        try {
            if (obj != null) {
                if ((obj instanceof Boolean)) {
                    return (Boolean) obj;
                }
                if ((obj instanceof String)) {
                    String s = (String) obj;
                    return (!"".equals(s.trim())) && (!s.equalsIgnoreCase(FALSE));
                } else if ((obj instanceof Number)) {
                    Number s = (Number) obj;
                    return s.doubleValue() > 9.999999974752427E-7D;
                }
                return true;
            }
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return false;
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static <T> T combineObject(T sourceBean, T targetBean) {
        Class sourceBeanClass = sourceBean.getClass();
        Class targetBeanClass = targetBean.getClass();

        Field[] sourceFields = sourceBeanClass.getDeclaredFields();
        Field[] targetFields = targetBeanClass.getDeclaredFields();
        for (int i = 0; i < sourceFields.length; i++) {
            Field sourceField = sourceFields[i];
            Field targetField = targetFields[i];
            sourceField.setAccessible(true);
            targetField.setAccessible(true);
            try {
                if ("serialVersionUID".equals(sourceField.getName())) {
                    continue;
                }
                if (!(sourceField.get(sourceBean) == null)) {
                    targetField.set(targetBean, sourceField.get(sourceBean));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return targetBean;
    }

    private static Map<String, Object> obj2Map(Object obj) {
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fields = obj.getClass().getDeclaredFields(); // 获取对象对应类中的所有属性域
        for (Field field : fields) {
            String varName = field.getName();
            varName = varName.toUpperCase();///将key置为大写，默认为对象的属性
            boolean accessFlag = field.isAccessible(); // 获取原来的访问控制权限
            field.setAccessible(true);// 修改访问控制权限
            try {
                Object object = field.get(obj); // 获取在对象中属性fields[i]对应的对象中的变量
                if (object != null) {
                    map.put(varName, object);
                } else {
                    map.put(varName, null);
                }
                field.setAccessible(accessFlag);// 恢复访问控制权限
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

}
