package com.yy.vokiller.paser;

import com.yy.vokiller.annotation.VOParam;
import com.yy.vokiller.exception.*;
import com.yy.vokiller.utils.AnnotationUtils;
import net.sf.cglib.beans.BeanGenerator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;

/**
 * @author yuanyang(417168602 @ qq.com)
 * @date 2019/3/18 10:49
 */
public class StructureGenerator {


    public static Structure generate(List<Token> tokenList, Class type, Method method, Object[] args) throws StatusException {
        Structure structure = null;
        List<Field> fieldList;
        Class clazz = null;
        if (type != null && type != Object.class) {
            structure = new Structure();
            structure.setType(type);
        } else if (tokenList != null && tokenList.size() != 0) {
            BeanGenerator generator = new BeanGenerator();
            structure = generateClass(generator, tokenList, method);
        } else if (args != null) {
            structure = generateClass(args, method);
        } else {
            throw new ArgumentNotNullException();
        }
        return structure;
    }


    private static Structure generateClass(BeanGenerator generator, List<Token> tokenList, Method method) {

        return null;
    }

    private static Structure generateClass(Object[] args, Method method) throws StatusException {

        Structure structure = new Structure();
        List<String> fieldList = new ArrayList<>();
        Annotation[][] annotations = method.getParameterAnnotations();
        //单个参数无需创建一级对象
        if (args.length == 1) {
            Object arg = args[0];
            VOParam voParam = AnnotationUtils.getParameterAnnotation(annotations[0], VOParam.class);
            List<String> includeArgNames = Arrays.asList(voParam.include());
            List<String> excludeArgNames = Arrays.asList(voParam.exclude());
            //都不为空抛异常
            if (!includeArgNames.isEmpty() && !excludeArgNames.isEmpty()) {
                throw new LogicErrorException();
            }
            //都为空直接返回对象类
            else if (includeArgNames.isEmpty() && excludeArgNames.isEmpty()) {
                for (Field field : arg.getClass().getDeclaredFields()) {
                    fieldList.add(field.getName());
                }
                structure.setType(arg.getClass());
                structure.setFieldList(fieldList);
                return structure;
            } else {
                return generateSingleClass(arg, includeArgNames, excludeArgNames);
            }
        }
        BeanGenerator generator = new BeanGenerator();
        Map<String, Structure> structureMap = new HashMap<>(16);
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            VOParam voParam = AnnotationUtils.getParameterAnnotation(annotations[i], VOParam.class);
            String fieldName = voParam.value();
            if (fieldName.isEmpty()) {
                throw new ArgNameNotSpecifyException();
            }
            List<String> includeArgNames = Arrays.asList(voParam.include());
            List<String> excludeArgNames = Arrays.asList(voParam.exclude());
            if (!includeArgNames.isEmpty() && !excludeArgNames.isEmpty()) {
                throw new LogicErrorException();
            } else if (includeArgNames.isEmpty() && excludeArgNames.isEmpty()) {
                generator.addProperty(fieldName, arg.getClass());
            } else {
                Structure singleStructure = generateSingleClass(arg, includeArgNames, excludeArgNames);
                structureMap.put(fieldName, singleStructure);
                generator.addProperty(fieldName, (Class) singleStructure.getGenerator().createClass());
            }
            fieldList.add(fieldName);
        }
        structure.setGenerator(generator);
        structure.setFieldList(fieldList);
        structure.setStructureMap(structureMap);
        return structure;
    }

    public static Structure generateSingleClass(Object obj, List<String> includeArgNames, List<String> excludeArgNames) {
        Structure structure = new Structure();
        List<String> fieldNameList = new ArrayList<>(16);
        Field[] fields = obj.getClass().getDeclaredFields();
        BeanGenerator singleGenerator = new BeanGenerator();
        for (int i = 0; i < fields.length; i++) {
            Field singleField = fields[i];
            String fieldName = singleField.getName();
            if (!includeArgNames.isEmpty() && includeArgNames.contains(fieldName)
                    || !excludeArgNames.isEmpty() && !excludeArgNames.contains(fieldName)) {
                fieldNameList.add(fieldName);
                singleGenerator.addProperty(fieldName, singleField.getType());
            }
        }
        structure.setGenerator(singleGenerator);
        structure.setFieldList(fieldNameList);
        return structure;
    }


}
