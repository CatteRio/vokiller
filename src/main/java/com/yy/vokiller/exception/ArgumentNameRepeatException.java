package com.yy.vokiller.exception;

/**
 * @author yuanyang(417168602 @ qq.com)
 * @date 2019/3/18 15:41
 */
public class ArgumentNameRepeatException extends StatusException {

    public ArgumentNameRepeatException(String name) {
        super("repeated argument name :" + name);
    }
}
