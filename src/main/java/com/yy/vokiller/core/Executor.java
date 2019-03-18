package com.yy.vokiller.core;

import com.yy.vokiller.annotation.VOParam;
import com.yy.vokiller.exception.ArgNameNotSpecifyException;
import com.yy.vokiller.exception.PropertyNotFindException;
import com.yy.vokiller.exception.StatusException;
import com.yy.vokiller.paser.Structure;
import com.yy.vokiller.utils.AnnotationUtils;
import net.sf.cglib.beans.BeanMap;
import org.omg.CORBA.OBJECT_NOT_EXIST;
import org.springframework.context.annotation.Bean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yuanyang(417168602 @ qq.com)
 * @date 2019/3/18 11:02
 */
public class Executor {
    private Structure structure;

    public Executor(Structure structure) {
        this.structure = structure;
    }

    public Object execute(Method method, Object[] args) throws StatusException {
        Map<String, Object> argsValueMap = getAllArgsValueMap(method, args);
        Object obj = execute(this.structure, argsValueMap);
        return obj;
    }

    private Object execute(Structure structure, Map<String, Object> argsValueMap) throws StatusException {
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
//            beanMap.putAll(argsValueMap);

            if (structureMap != null) {
                for (String fieldName : structureMap.keySet()) {
                    Structure subStructure = structureMap.get(fieldName);
                    Map<String, Object> newArgsValueMap = getAllArgsValueMap(null, new Object[]{argsValueMap.get(fieldName)});
                    beanMap.put(fieldName, execute(subStructure, newArgsValueMap));
                }
            }
            return returnJob;
        } else {
            return null;
        }
    }

    private Map<String, Object> getAllArgsValueMap(Method method, Object[] args) throws StatusException {
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
            Annotation[][] annotations = method.getParameterAnnotations();
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


    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }
}
