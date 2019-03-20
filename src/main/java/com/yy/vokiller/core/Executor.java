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

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }
}
