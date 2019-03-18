package com.yy.vokiller.exception;

/**
 * @author yuanyang(417168602 @ qq.com)
 * @date 2019/3/18 13:06
 */
public class PropertyNotFindException extends StatusException {

    public PropertyNotFindException(String propertyName) {
        super("property " + propertyName + " can not find in argument list");
    }
}
