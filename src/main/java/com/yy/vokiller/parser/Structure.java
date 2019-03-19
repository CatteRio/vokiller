package com.yy.vokiller.parser;

import com.yy.vokiller.annotation.VOParam;
import net.sf.cglib.beans.BeanGenerator;

import java.lang.annotation.Annotation;
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

    private Map<VOParam, Integer/*Position*/> annoPositionMap;
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

    public Map<VOParam, Integer> getAnnoPositionMap() {
        return annoPositionMap;
    }

    public void setAnnoPositionMap(Map<VOParam, Integer> annoPositionMap) {
        this.annoPositionMap = annoPositionMap;
    }

    public Class getActiveClassType() {
        if (this.generator != null) {
            return (Class) this.generator.createClass();
        } else if (this.type != null) {
            return this.type;
        } else {
            return null;
        }
    }
}
