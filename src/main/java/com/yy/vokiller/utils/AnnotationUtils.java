package com.yy.vokiller.utils;

import java.lang.annotation.Annotation;

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
}
