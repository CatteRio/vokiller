package com.yy.vokiller;


import com.yy.annotation.VoConfigure;
import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.beans.BeanMap;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class VoKiller {

    public static <T> Object getVO(Class<T> clazz, T obj, String value, Map<String, Object> properties) {
        BeanGenerator generator = new BeanGenerator();
        Map<String, Object> valuesMap = new HashMap<String, Object>(16);
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            // 获得字段注解
            VoConfigure annotation = field.getAnnotation(VoConfigure.class);
            if (annotation != null && isContains((String[]) annotation.value(), value)) {
                generator.addProperty(field.getName(), field.getType());
                try {
                    valuesMap.put(field.getName(), field.get(obj));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (properties != null) {
            for (String key : properties.keySet()) {
                Object property = properties.get(key);
                generator.addProperty(key, property.getClass());
                valuesMap.put(key, property);
            }
        }
        Object object = generator.create();
        BeanMap beanMap = BeanMap.create(object);
        beanMap.putAll(valuesMap);
        return object;
    }

    public static <T> Object getVO(Class<T> clazz, T obj, String value) {
        return getVO(clazz, obj, value, null);
    }

    private static Boolean isContains(String[] source, String target) {
        for (String value :
                source) {
            if (value.equals(target)) {
                return true;
            }
        }
        return false;
    }
}
