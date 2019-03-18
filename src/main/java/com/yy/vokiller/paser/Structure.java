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


    private List<Field> fieldList;

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public List<Field> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<Field> fieldList) {
        this.fieldList = fieldList;
    }


    private BeanGenerator generator;

}
