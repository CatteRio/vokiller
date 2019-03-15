package com.yy.vokiller;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

/**
 * @author: yuanyang(417168602 @ qq.com)
 * @date: 2019-03-15 20:01
 **/
public class MapperFactoryBean<T> implements FactoryBean<T> {
    private String innerClassName;

    public void setInnerClassName(String innerClassName) {
        this.innerClassName = innerClassName;
    }
    private MapperProxy mapperProxy = new MapperProxy();
    public T getObject() throws Exception {
        Class innerClass = Class.forName(innerClassName);
        return (T) Proxy.newProxyInstance(innerClass.getClassLoader(), new Class[]{innerClass}, mapperProxy);
    }
    public Class<?> getObjectType() {
        try {
            return Class.forName(innerClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean isSingleton() {
        return true;
    }
}
