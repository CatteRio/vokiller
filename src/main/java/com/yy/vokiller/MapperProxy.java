package com.yy.vokiller;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author: yuanyang(417168602 @ qq.com)
 * @date: 2019-03-15 20:00
 **/
public class MapperProxy implements InvocationHandler {
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("gogogogogogo");
        return null;
    }
}
