package com.yy.vokiller.core;

import com.yy.vokiller.annotation.SelectVO;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author: yuanyang(417168602 @ qq.com)
 * @date: 2019-03-15 20:00
 **/
public class VOProxy implements InvocationHandler {


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(proxy,args);
        }
        SelectVO selectVO =  method.getDeclaredAnnotation(SelectVO.class);
        Class<?> returnType = method.getReturnType();
        if(returnType == Object.class){
            return "Object";
        }
        String vql = selectVO.vql();
        return null;
    }
}
