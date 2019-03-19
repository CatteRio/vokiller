package com.yy.vokiller.exception;

/**
 * @author yuanyang(417168602 @ qq.com)
 * @date 2019/3/19 13:07
 */
public class TooManyValuesException extends StatusException {

    public TooManyValuesException(String argName) {
        super("arg '" + argName + "' has too many values");
    }
}
