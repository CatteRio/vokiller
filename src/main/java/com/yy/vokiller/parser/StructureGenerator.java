package com.yy.vokiller.parser;

import com.yy.vokiller.annotation.VOParam;
import com.yy.vokiller.exception.*;
import com.yy.vokiller.utils.AnnotationEntry;
import com.yy.vokiller.utils.AnnotationUtils;
import com.yy.vokiller.utils.CollectionUtils;
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
        if (type != null && type != Object.class) {
            structure = generateSpecifyClass(type, method);
        } else if (tokenList != null && tokenList.size() != 0) {
            structure = generateClass(tokenList, method);
        } else if (args != null) {
            structure = generateClass(args, method);
        } else {
            throw new ArgumentNotNullException();
        }
        return structure;
    }

    private static Structure generateSpecifyClass(Class type, Method method) throws StatusException {
        Structure structure = new Structure();
        structure.setType(type);
        Map<String, Map.Entry<VOParam, Class>> fieldNameMetaDataMap = AnnotationUtils.getVoParamMapping(method);
        Map<VOParam, Integer> annoParamMap = new HashMap<>(16);
        for (Map.Entry<String, Map.Entry<VOParam, Class>> entry : fieldNameMetaDataMap.entrySet()) {
            AnnotationEntry<VOParam, Class> entry2 = (AnnotationEntry) entry.getValue();
            annoParamMap.put(entry2.getKey(), entry2.getPosition());
        }
        structure.setAnnoPositionMap(annoParamMap);
        return structure;
    }

    private static Structure generateClass(List<Token> tokenList, Method method) throws StatusException {
        Structure structure = new Structure();
        List<String> fieldList = new ArrayList<>();
        Annotation[][] annotations = method.getParameterAnnotations();
        Map<String, Map.Entry<VOParam, Class>> fieldNameMetaDataMap = AnnotationUtils.getVoParamMapping(method);
        return generateStructure(tokenList, fieldNameMetaDataMap);
    }

    private static Structure generateStructure(List<Token> tokenList, Map<String, Map.Entry<VOParam, Class>> fieldNameMetaDataMap) throws StatusException {
        Structure structure = new Structure();
        List<String> fieldList = new ArrayList<>(16);
        Map<VOParam, Integer> annoParamMap = new HashMap<>(16);
        if (tokenList.size() == 1) {
            Token token = tokenList.get(0);
            String fieldName = token.getFieldName();
            String valueName = token.getFieldValue();
            if (!fieldNameMetaDataMap.containsKey(valueName)) {
                throw new PropertyNotFindException(valueName);
            }
            //根据value指定的值获取参数
            Map.Entry<VOParam, Class> entry = fieldNameMetaDataMap.get(valueName);
            //TODO:处理直接赋值
//            if(entry == null){
//                AnnotationEntry<VOParam, Class> newEntry = new AnnotationEntry<>();
//                newEntry.setKey(new VO);
//            }
            VOParam voParam = entry.getKey();
            Class clazz = entry.getValue();
            Integer position = ((AnnotationEntry) entry).getPosition();
            List<String> includeArgNames = AnnotationUtils.getIncludeArgNames(voParam);
            List<String> excludeArgNames = AnnotationUtils.getExcludeArgNames(voParam);

            //都不为空抛异常
            if (!includeArgNames.isEmpty() && !excludeArgNames.isEmpty()) {
                throw new LogicErrorException();
            }
            //都为空直接返回对象类
            else if (includeArgNames.isEmpty() && excludeArgNames.isEmpty() && CollectionUtils.isEmpty(token.getSubFields())) {
                for (Field field : clazz.getDeclaredFields()) {
                    fieldList.add(field.getName());
                }
                structure.setType(clazz);
                structure.setFieldList(fieldList);
                annoParamMap.put(voParam, position);
                structure.setAnnoPositionMap(annoParamMap);
                return structure;
            } else {
                BeanGenerator beanGenerator = new BeanGenerator();
                Map<String, Structure> structureMap = new HashMap<>(16);
                Structure structure1 = generateStructure(token.getSubFields(), fieldNameMetaDataMap);
                for (String name : structure1.getStructureMap().keySet()) {
                    Structure structure2 = structure1.getStructureMap().get(name);
                    beanGenerator.addProperty(name, structure2.getActiveClassType());
                    fieldList.add(name);
                }
                for (Field field : clazz.getDeclaredFields()) {
                    if (includeArgNames.isEmpty() && excludeArgNames.isEmpty()
                            || !includeArgNames.isEmpty() && includeArgNames.contains(field.getName())
                            || !excludeArgNames.isEmpty() && !excludeArgNames.contains(field.getName())) {
                        try {
                            beanGenerator.addProperty(field.getName(), field.getType());
                            fieldList.add(field.getName());
                        } catch (IllegalArgumentException e) {
                        }
                    }
                }

                annoParamMap.put(voParam, position);
                structure.setGenerator(beanGenerator);
                structure.setStructureMap(structure1.getStructureMap());
                structure.setFieldList(fieldList);
                structure.setAnnoPositionMap(annoParamMap);
                return structure;
            }
        } else {
            Map<String, Structure> structureMap = new HashMap<>(16);
            BeanGenerator generator = new BeanGenerator();
            for (int i = 0; i < tokenList.size(); i++) {
                Token token = tokenList.get(i);
                List<Token> singleTokenList = new ArrayList<>();
                singleTokenList.add(token);
                Structure structure1 = generateStructure(singleTokenList, fieldNameMetaDataMap);
                fieldList.add(token.getFieldName());
                structureMap.put(token.getFieldName(), structure1);
                generator.addProperty(token.getFieldName(), structure1.getActiveClassType());
            }
            structure.setGenerator(generator);
            structure.setStructureMap(structureMap);
            structure.setFieldList(fieldList);
            structure.setAnnoPositionMap(annoParamMap);
            return structure;
        }
    }

    private static Structure generateClass(Object[] args, Method method) throws StatusException {

        Structure structure = new Structure();
        List<String> fieldList = new ArrayList<>();
        Annotation[][] annotations = method.getParameterAnnotations();
        Map<VOParam, Integer> annoParamMap = new HashMap<>(16);
        //单个参数无需创建一级对象
        if (args.length == 1) {
            Object arg = args[0];
            VOParam voParam = AnnotationUtils.getParameterAnnotation(annotations[0], VOParam.class);
            List<String> includeArgNames = AnnotationUtils.getIncludeArgNames(voParam);
            List<String> excludeArgNames = AnnotationUtils.getExcludeArgNames(voParam);
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
                annoParamMap.put(voParam, 0);
                structure.setAnnoPositionMap(annoParamMap);
                return structure;
            } else {
                Structure structure1 = generateSingleClass(arg, includeArgNames, excludeArgNames);
                annoParamMap.put(voParam, 0);
                structure1.setAnnoPositionMap(annoParamMap);
                return structure1;
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

            List<String> includeArgNames = AnnotationUtils.getIncludeArgNames(voParam);
            List<String> excludeArgNames = AnnotationUtils.getExcludeArgNames(voParam);
            if (!includeArgNames.isEmpty() && !excludeArgNames.isEmpty()) {
                throw new LogicErrorException();
            } else if (includeArgNames.isEmpty() && excludeArgNames.isEmpty()) {
                annoParamMap.put(voParam, i);
                generator.addProperty(fieldName, arg.getClass());
            } else {
                Structure singleStructure = generateSingleClass(arg, includeArgNames, excludeArgNames);
                Map<VOParam, Integer> singleParamIntegerMap = new HashMap<>(16);
                singleParamIntegerMap.put(voParam, i);
                singleStructure.setAnnoPositionMap(singleParamIntegerMap);
                structureMap.put(fieldName, singleStructure);
                generator.addProperty(fieldName, (Class) singleStructure.getGenerator().createClass());
            }
            fieldList.add(fieldName);
        }
        structure.setGenerator(generator);
        structure.setFieldList(fieldList);
        structure.setStructureMap(structureMap);
        structure.setAnnoPositionMap(annoParamMap);
        return structure;
    }

    public static Structure generateSingleClass(Object obj, List<String> includeArgNames, List<String> excludeArgNames) {
        Structure structure = new Structure();
        List<String> fieldNameList = new ArrayList<>(16);
        Field[] fields = obj.getClass().getDeclaredFields();
        Map<VOParam, Integer> annoParamMap = new HashMap<>(16);
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
