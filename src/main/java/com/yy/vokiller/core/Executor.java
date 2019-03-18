package com.yy.vokiller.core;

import com.yy.vokiller.annotation.VOParam;
import com.yy.vokiller.exception.*;
import com.yy.vokiller.paser.Structure;
import com.yy.vokiller.utils.AnnotationUtils;
import com.yy.vokiller.utils.BeanUtils;
import net.sf.cglib.beans.BeanMap;
import org.omg.CORBA.OBJECT_NOT_EXIST;
import org.springframework.context.annotation.Bean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
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
        Object[] filterArgs = filterArgs(args);
        Map<String, Object> argsValueMap = getAllArgsValueMap(filterArgs);
        Object obj = execute(this.structure, argsValueMap);
        return obj;
    }

    private Object execute(Structure structure, Map<String, Object> argsValueMap) throws StatusException {
        //由bean generator生成
        if (structure.getType() == null) {
            Object returnJob = structure.getGenerator().create();
            BeanMap beanMap = BeanMap.create(returnJob);
            Map<String, Structure> structureMap = structure.getStructureMap();
            for (String fieldName : structure.getFieldList()) {
                if (!argsValueMap.containsKey(fieldName)) {
                    throw new PropertyNotFindException(fieldName);
                }
                if (structureMap == null || !structureMap.containsKey(fieldName)) {
                    Object value = argsValueMap.get(fieldName);
                    beanMap.put(fieldName, value);
                }
            }
            if (structureMap != null) {
                for (String fieldName : structureMap.keySet()) {
                    Structure subStructure = structureMap.get(fieldName);
                    Map<String, Object> newArgsValueMap = getAllArgsValueMap(new Object[]{argsValueMap.get(fieldName)});
                    beanMap.put(fieldName, execute(subStructure, newArgsValueMap));
                }
            }
            return returnJob;
        }
        //由class对象生成
        else {
            Class clazz = structure.getType();
            try {
                Object returnJob = clazz.newInstance();
                for (Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                    field.set(returnJob, argsValueMap.get(field.getName()));
                }
                return returnJob;
            } catch (Exception e) {
                throw new CreateBeanException("error while create bean :" + clazz);
            }
        }
    }

    private Map<String, Object> getAllArgsValueMap(Object[] args) throws StatusException {
        Map<String, Object> argsValueMap = new HashMap<>(16);
        if (args.length == 1) {
            Object arg = args[0];
            Field[] fields = arg.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    argsValueMap.put(field.getName(), field.get(arg));
                } catch (IllegalAccessException e) {
                    argsValueMap.put(field.getName(), null);
                }
            }
        } else {
            Annotation[][] annotations = this.method.getParameterAnnotations();
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                VOParam voParam = AnnotationUtils.getParameterAnnotation(annotations[i], VOParam.class);
                String fieldName = voParam.value();
                if (fieldName.isEmpty()) {
                    throw new ArgNameNotSpecifyException();
                }
                argsValueMap.put(fieldName, arg);
            }
        }
        return argsValueMap;
    }


    private Object[] filterArgs(final Object[] args) throws StatusException {
        Object[] newArgs = new Object[args.length];
        Annotation[][] annotations = this.method.getParameterAnnotations();
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            Object newArg = null;
            try {
                newArg = BeanUtils.deepClone(arg);
            } catch (Exception e) {
                throw new UnknownException(e);
            }
            VOParam voParam = AnnotationUtils.getParameterAnnotation(annotations[i], VOParam.class);
            List<String> includeArgNames = Arrays.asList(voParam.include());
            List<String> excludeArgNames = Arrays.asList(voParam.exclude());
            if (!includeArgNames.isEmpty() && !excludeArgNames.isEmpty()) {
                throw new LogicErrorException();
            }

            for (Field field : newArg.getClass().getDeclaredFields()) {
                String fieldName = field.getName();
                if (!includeArgNames.isEmpty() && !includeArgNames.contains(fieldName)
                        || !excludeArgNames.isEmpty() && excludeArgNames.contains(fieldName)) {
                    field.setAccessible(true);
                    try {
                        field.set(newArg, null);
                    } catch (IllegalAccessException e) {
                        throw new UnknownException(e);
                    }
                }
            }
            newArgs[i] = newArg;
        }
        return newArgs;
    }

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }
}
