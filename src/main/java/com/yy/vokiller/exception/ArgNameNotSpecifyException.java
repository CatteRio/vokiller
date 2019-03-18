package com.yy.vokiller.exception;

/**
 * @author yuanyang(417168602 @ qq.com)
 * @date 2019/3/18 15:58
 */
public class ArgNameNotSpecifyException extends StatusException {

    public ArgNameNotSpecifyException() {
        super("all args should be specified a name");
    }
}
