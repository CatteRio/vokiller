package com.yy.vokiller.exception;

/**
 * @author yuanyang(417168602 @ qq.com)
 * @date 2019/3/18 13:16
 */
public class LogicErrorException extends StatusException {

    public LogicErrorException() {
        super("include and exclude can not appear at same time");
    }
}
