package com.yy.vokiller.exception;

/**
 * @author yuanyang(417168602 @ qq.com)
 * @date 2019/3/18 18:31
 */
public class UnknownException extends StatusException {

    public UnknownException(Exception e) {
        super(e);
    }
}
