package com.yy.vokiller.core;

import com.yy.vokiller.paser.Structure;

import java.lang.reflect.Field;

/**
 * @author yuanyang(417168602 @ qq.com)
 * @date 2019/3/18 11:02
 */
public class Executor {
    private Structure structure;

    public Executor(Structure structure) {
        this.structure = structure;
        for (Field field : structure.getFieldList()) {
            System.out.println(field.getName());
        }
    }

    public Object execute(Object[] args) {

        return null;
    }


    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }
}
