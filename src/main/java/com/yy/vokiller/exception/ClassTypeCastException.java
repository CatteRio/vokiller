package com.yy.vokiller.exception;

/**
 * @author yuanyang(417168602 @ qq.com)
 * @date 2019/3/19 13:54
 */
public class ClassTypeCastException extends StatusException {

    public ClassTypeCastException(Class targetType) {
        super("can not generate " + targetType.getName());
    }
}
