package com.yy.vokiller.utils;

import com.yy.vokiller.annotation.VOParam;
import com.yy.vokiller.exception.ArgNameNotSpecifyException;
import com.yy.vokiller.exception.ArgumentNameRepeatException;
import com.yy.vokiller.exception.ArgumentNotNullException;
import com.yy.vokiller.exception.StatusException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yuanyang(417168602 @ qq.com)
 * @date 2019/3/18 15:39
 */
public class AnnotationUtils {

    public static <A extends Annotation> A getParameterAnnotation(Annotation[] annotations, Class<A> annotationType) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == annotationType) {
                return (A) annotation;
            }
        }
        return null;
    }


    public static <A extends Annotation> List<A> getParameterAnnotationList(Annotation[][] annotations, Class<A> annotationType) {
        List<A> aList = new ArrayList<>(16);
        for (Annotation[] annotationsList : annotations) {
            A annotation = getParameterAnnotation(annotationsList, annotationType);
            if (annotation != null) {
                aList.add(annotation);
            }
        }
        return aList;
    }

    public static Map<String, Map.Entry<VOParam, Class>> getVoParamMapping(Method method) throws StatusException {
        Annotation[][] annotations = method.getParameterAnnotations();
        Class[] clazzArray = method.getParameterTypes();
        Map<String, Map.Entry<VOParam, Class>> fieldNameMetaDataMap = new HashMap<>(16);
        if (clazzArray.length < 1) {
            throw new ArgumentNotNullException();
        }
        if (clazzArray.length == 1) {
            Annotation[] annotations1 = annotations[0];
            VOParam annotation = getParameterAnnotation(annotations1, VOParam.class);
            String parameterName = annotation == null ? null : annotation.value();
            Class clazz = clazzArray[0];
            AnnotationEntry<VOParam, Class> entry = new AnnotationEntry<>();
            entry.setKey(annotation);
            entry.setValue(clazz);
            entry.setPosition(0);
            fieldNameMetaDataMap.put(parameterName, entry);
        } else {
            for (int i = 0; i < annotations.length; i++) {
                Annotation[] annotations1 = annotations[i];
                VOParam annotation = getParameterAnnotation(annotations1, VOParam.class);
                if (annotation != null && !StringUtils.isEmpty(annotation.value())) {
                    String parameterName = annotation.value();
                    if (fieldNameMetaDataMap.containsKey(parameterName)) {
                        throw new ArgumentNameRepeatException(parameterName);
                    }
                    Class clazz = clazzArray[i];
                    AnnotationEntry<VOParam, Class> entry = new AnnotationEntry<>();
                    entry.setKey(annotation);
                    entry.setValue(clazz);
                    entry.setPosition(i);
                    fieldNameMetaDataMap.put(parameterName, entry);
                } else {
                    throw new ArgNameNotSpecifyException();
                }
            }
        }
        return fieldNameMetaDataMap;
    }
}
