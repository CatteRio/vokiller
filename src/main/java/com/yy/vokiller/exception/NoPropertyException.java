package com.yy.vokiller.exception;

public class NoPropertyException extends Exception {

    private String property;

    public NoPropertyException(String property) {
        super("can not find property named \"" + property + "\"");
        this.property = property;

    }


}
