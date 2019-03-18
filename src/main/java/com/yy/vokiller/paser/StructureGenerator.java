package com.yy.vokiller.paser;

import com.yy.vokiller.annotation.VOParam;
import com.yy.vokiller.exception.ArgumentNotNullException;
import com.yy.vokiller.exception.LogicErrorException;
import com.yy.vokiller.exception.PropertyNotFindException;
import com.yy.vokiller.exception.StatusException;
import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.beans.BeanMap;
import net.sf.cglib.proxy.Enhancer;

import java.awt.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * @author yuanyang(417168602 @ qq.com)
 * @date 2019/3/18 10:49
 */
public class StructureGenerator {


    public static Structure generate(List<Token> tokenList, Class type, Method method, Object[] args) throws StatusException {
        Structure structure = new Structure();
        List<Field> fieldList;
        Class clazz = null;
        if (type != null && type != Object.class) {
            clazz = type;
        } else if (tokenList != null && tokenList.size() != 0) {
            BeanGenerator generator = new BeanGenerator();
            clazz = generateClass(generator, tokenList, method);
        } else if (args != null) {
            clazz = generateClass(args, method);
        } else {
            throw new ArgumentNotNullException();
        }
        fieldList = Arrays.asList(clazz.getDeclaredFields());
        structure.setFieldList(fieldList);
        structure.setType(clazz);
        return structure;
    }


    private static Class generateClass(BeanGenerator generator, List<Token> tokenList, Method method) {

        return null;
    }

    private static Class generateClass(Object[] args, Method method) throws StatusException {

        Annotation[][] annotations = method.getParameterAnnotations();
        //单个参数无需创建一级对象
        if (args.length == 1) {
            Object arg = args[0];
            VOParam voParam = getParameterAnnotation(annotations[0], VOParam.class);
            List<String> includeArgNames = Arrays.asList(voParam.include());
            List<String> excludeArgNames = Arrays.asList(voParam.exclude());
            //都不为空抛异常
            if (!includeArgNames.isEmpty() && !excludeArgNames.isEmpty()) {
                throw new LogicErrorException();
            }
            //都为空直接返回对象类
            else if (includeArgNames.isEmpty() && excludeArgNames.isEmpty()) {
                return arg.getClass();
            } else {
                return generateSingleClass(arg, includeArgNames, excludeArgNames);
            }
        }
        //
        BeanGenerator generator = new BeanGenerator();
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            VOParam voParam = arg.getClass().getAnnotation(VOParam.class);
            String fieldName = voParam.value();
            if (fieldName.isEmpty()) {
                throw new PropertyNotFindException(fieldName);
            }
            List<String> includeArgNames = Arrays.asList(voParam.include());
            List<String> excludeArgNames = Arrays.asList(voParam.exclude());
            if (!includeArgNames.isEmpty() && !excludeArgNames.isEmpty()) {
                throw new LogicErrorException();
            } else if (includeArgNames.isEmpty() && excludeArgNames.isEmpty()) {
                generator.addProperty(fieldName, arg.getClass());
            } else {
                generator.addProperty(fieldName, generateSingleClass(arg, includeArgNames, excludeArgNames));
            }
        }
        return generator.create().getClass();
    }

    public static Class generateSingleClass(Object obj, List<String> includeArgNames, List<String> excludeArgNames) {
        Field[] fields = obj.getClass().getDeclaredFields();
        BeanGenerator singleGenerator = new BeanGenerator();
        for (int i = 0; i < fields.length; i++) {
            Field singleField = fields[i];
            String fieldName = singleField.getName();
            if (!includeArgNames.isEmpty() && includeArgNames.contains(fieldName)) {
                singleGenerator.addProperty(fieldName, singleField.getType());
            } else if (!excludeArgNames.isEmpty() && !excludeArgNames.contains(fieldName)) {
                singleGenerator.addProperty(fieldName, singleField.getType());
            }
        }
        Object ssss = singleGenerator.create();
        BeanMap beanMap = BeanMap.create(ssss);
        beanMap.put("name","123456");
        return singleGenerator.create().getClass();
    }


    public static <A extends Annotation> A getParameterAnnotation(Annotation[] annotations, Class<A> annotationType) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == annotationType) {
                return (A) annotation;
            }
        }
        return null;
    }
}
