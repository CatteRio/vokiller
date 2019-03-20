package com.yy.vokiller.core;

import com.yy.vokiller.annotation.VoParam;
import com.yy.vokiller.exception.*;
import com.yy.vokiller.parser.Structure;
import com.yy.vokiller.utils.AnnotationUtils;
import com.yy.vokiller.utils.CollectionUtils;
import net.sf.cglib.beans.BeanMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yuanyang(417168602 @ qq.com)
 * @date 2019/3/18 11:02
 */
public class Executor {
    private Structure structure;
    private Method method;

    public Executor(Structure structure, Method method) {
        this.structure = structure;
        this.method = method;
    }

    public Object execute(Object[] args) throws StatusException {
//        Object[] filterArgs = filterArgs(args);
//        Map<String, Object> argsValueMap = getAllArgsValueMap(filterArgs);
//        Object obj = execute(this.structure, argsValueMap);

        Object obj = execute(this.structure, args);
        return obj;
    }

    private Object execute(Structure structure, Object[] args) throws StatusException {
        Map<VoParam, Integer> voParamIntegerMap = structure.getAnnoPositionMap();
        Map<String, Object> argsValueMap = getAllArgsValueMap(args, voParamIntegerMap);
        //由bean generator生成
        if (structure.getType() == null) {
            Object returnJob = structure.getGenerator().create();
            BeanMap beanMap = BeanMap.create(returnJob);
            Map<String, Structure> structureMap = structure.getStructureMap();
            for (String fieldName : structure.getFieldList()) {
                //不在structureMap里，就是基本属性需要赋值
                if (!CollectionUtils.isContainsKey(structureMap, fieldName)) {
                    if (!argsValueMap.containsKey(fieldName)) {
                        throw new PropertyNotFindException(fieldName);
                    } else {
                        Object value = argsValueMap.get(fieldName);
                        beanMap.put(fieldName, value);
                    }
                }
            }
            if (structureMap != null) {
                for (String fieldName : structureMap.keySet()) {
                    Structure subStructure = structureMap.get(fieldName);
                    beanMap.put(fieldName, execute(subStructure, args));
                }
            }
            return returnJob;
        }
        //由class对象生成
        else {
            Class clazzType = structure.getType();
            Object returnJob = null;
            if (voParamIntegerMap.size() == 1) {
                Object arg = null;
                for (Map.Entry<VoParam, Integer> voParam : voParamIntegerMap.entrySet()) {
                    arg = args[voParam.getValue()];
                    break;
                }
                //如果参数是其子类或者子接口
                if (clazzType.isAssignableFrom(arg.getClass())) {
                    returnJob = arg;
                    return returnJob;
                }
            }
//            //如果是基本对象 可以直接赋值
//            //如果参数列表参数大于1说明有错误
//            if (voParamIntegerMap.keySet().size() > 1) {
//                throw new TooManyValuesException(structure.getType().toString());
//            }
            if (!clazzType.isInterface()) {
                try {
                    Object obj = clazzType.newInstance();
                    for (Field field : clazzType.getDeclaredFields()) {
                        field.setAccessible(true);
                        field.set(obj, argsValueMap.get(field.getName()));
                    }
                    returnJob = obj;
                } catch (Exception e) {
                    throw new UnknownException(e);
                }
            } else {
                throw new ClassTypeCastException(clazzType);
            }
            return returnJob;
        }
    }

    private Map<String, Object> getAllArgsValueMap(final Object[] args, Map<VoParam, Integer> voParamIntegerMap) throws StatusException {
        Map<String, Object> argsValueMap = new HashMap<>(16);
        if (voParamIntegerMap == null) {
            return argsValueMap;
        }
        for (VoParam voParam : voParamIntegerMap.keySet()) {
            Object arg = args[voParamIntegerMap.get(voParam)];
            if (voParam == null) {
                for (Field field : arg.getClass().getDeclaredFields()) {
                    String fieldName = field.getName();
                    field.setAccessible(true);
                    try {
                        argsValueMap.put(fieldName, field.get(arg));
                    } catch (IllegalAccessException e) {
                        throw new UnknownException(e);
                    }
                }
            } else {
                List<String> includeArgNames = AnnotationUtils.getIncludeArgNames(voParam);
                List<String> excludeArgNames = AnnotationUtils.getExcludeArgNames(voParam);
                if (!includeArgNames.isEmpty() && !excludeArgNames.isEmpty()) {
                    throw new LogicErrorException();
                } else if (includeArgNames.isEmpty() && excludeArgNames.isEmpty()) {
                    argsValueMap.put(voParam.value(), arg);
                } else {
                    for (Field field : arg.getClass().getDeclaredFields()) {
                        String fieldName = field.getName();
                        if (!includeArgNames.isEmpty() && includeArgNames.contains(fieldName)
                                || !excludeArgNames.isEmpty() && !excludeArgNames.contains(fieldName)) {
                            field.setAccessible(true);
                            try {
                                argsValueMap.put(fieldName, field.get(arg));
                            } catch (IllegalAccessException e) {
                                throw new UnknownException(e);
                            }
                        }
                    }
                }
            }
        }
        return argsValueMap;
    }

//    private Object execute(Structure structure, Map<String, Object> argsValueMap) throws StatusException {
//        //由bean generator生成
//        if (structure.getType() == null) {
//            Object returnJob = structure.getGenerator().create();
//            BeanMap beanMap = BeanMap.create(returnJob);
//            Map<String, Structure> structureMap = structure.getStructureMap();
//            for (String fieldName : structure.getFieldList()) {
//                if (!argsValueMap.containsKey(fieldName)) {
//                    throw new PropertyNotFindException(fieldName);
//                }
//                if (structureMap == null || !structureMap.containsKey(fieldName)) {
//                    Object value = argsValueMap.get(fieldName);
//                    beanMap.put(fieldName, value);
//                }
//            }
//            if (structureMap != null) {
//                for (String fieldName : structureMap.keySet()) {
//                    Structure subStructure = structureMap.get(fieldName);
//                    Map<String, Object> newArgsValueMap = getAllArgsValueMap(new Object[]{argsValueMap.get(fieldName)});
//                    beanMap.put(fieldName, execute(subStructure, newArgsValueMap));
//                }
//            }
//            return returnJob;
//        }
//        //由class对象生成
//        else {
//            Class clazz = structure.getType();
//            try {
//                Object returnJob = clazz.newInstance();
//                for (Field field : clazz.getDeclaredFields()) {
//                    field.setAccessible(true);
//                    field.set(returnJob, argsValueMap.get(field.getName()));
//                }
//                return returnJob;
//            } catch (Exception e) {
//                throw new CreateBeanException("error while create bean :" + clazz);
//            }
//        }
//    }

//    private Map<String, Object> getAllArgsValueMap(Object[] args) throws StatusException {
//        Map<String, Object> argsValueMap = new HashMap<>(16);
//        if (args.length == 1) {
//            Object arg = args[0];
//            Field[] fields = arg.getClass().getDeclaredFields();
//            for (Field field : fields) {
//                field.setAccessible(true);
//                try {
//                    argsValueMap.put(field.getName(), field.get(arg));
//                } catch (IllegalAccessException e) {
//                    argsValueMap.put(field.getName(), null);
//                }
//            }
//        } else {
//            Annotation[][] annotations = this.method.getParameterAnnotations();
//            for (int i = 0; i < args.length; i++) {
//                Object arg = args[i];
//                VoParam voParam = AnnotationUtils.getParameterAnnotation(annotations[i], VoParam.class);
//                String fieldName = voParam.value();
//                if (fieldName.isEmpty()) {
//                    throw new ArgNameNotSpecifyException();
//                }
//                argsValueMap.put(fieldName, arg);
//            }
//        }
//        return argsValueMap;
//    }


//    private Object[] filterArgs(final Object[] args) throws StatusException {
//        Object[] newArgs = new Object[args.length];
//        Annotation[][] annotations = this.method.getParameterAnnotations();
//        for (int i = 0; i < args.length; i++) {
//            Object arg = args[i];
//            Object newArg = null;
//            try {
//                newArg = BeanUtils.deepClone(arg);
//            } catch (Exception e) {
//                throw new UnknownException(e);
//            }
//            VoParam voParam = AnnotationUtils.getParameterAnnotation(annotations[i], VoParam.class);
//            List<String> includeArgNames = Arrays.asList(voParam.include());
//            List<String> excludeArgNames = Arrays.asList(voParam.exclude());
//            if (!includeArgNames.isEmpty() && !excludeArgNames.isEmpty()) {
//                throw new LogicErrorException();
//            }
//
//            for (Field field : newArg.getClass().getDeclaredFields()) {
//                String fieldName = field.getName();
//                if (!includeArgNames.isEmpty() && !includeArgNames.contains(fieldName)
//                        || !excludeArgNames.isEmpty() && excludeArgNames.contains(fieldName)) {
//                    field.setAccessible(true);
//                    try {
//                        field.set(newArg, null);
//                    } catch (IllegalAccessException e) {
//                        throw new UnknownException(e);
//                    }
//                }
//            }
//            newArgs[i] = newArg;
//        }
//        return newArgs;
//    }

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }
}
