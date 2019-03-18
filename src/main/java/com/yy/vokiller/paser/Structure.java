package com.yy.vokiller.paser;

import net.sf.cglib.beans.BeanGenerator;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @author yuanyang(417168602 @ qq.com)
 * @date 2019/3/18 10:49
 */
public class Structure {
    private Class type;

    private BeanGenerator generator;

    private List<String> fieldList;

    /**
     * 某些属性为自定义bean
     */
    private Map<String, Structure> structureMap;

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public BeanGenerator getGenerator() {
        return generator;
    }

    public void setGenerator(BeanGenerator generator) {
        this.generator = generator;
    }

    public List<String> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<String> fieldList) {
        this.fieldList = fieldList;
    }

    public Map<String, Structure> getStructureMap() {
        return structureMap;
    }

    public void setStructureMap(Map<String, Structure> structureMap) {
        this.structureMap = structureMap;
    }
}
