package com.yy.vokiller.core;

import com.yy.vokiller.paser.Structure;

/**
 * @author yuanyang(417168602 @ qq.com)
 * @date 2019/3/18 11:02
 */
public class Executor {
    private Structure structure;
    public Executor(Structure structure) {
        this.structure = structure;
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
