package com.yy.vokiller.core;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

/**
 * @author: yuanyang(417168602 @ qq.com)
 * @date: 2019-03-15 20:01
 **/
public class VoFactoryBean<T> implements FactoryBean<T> {
    private Class innerClassName;
    public VoFactoryBean(Class<T> mapperInterface) {
        this.innerClassName = mapperInterface;
    }
//    public void setInnerClassName(String innerClassName) {
//        this.innerClassName = innerClassName;
//    }

    private VoProxy mapperProxy = new VoProxy();

    @Override
    public T getObject() throws Exception {
        //Class innerClass = Class.forName(innerClassName);
        return (T) Proxy.newProxyInstance(innerClassName.getClassLoader(), new Class[]{innerClassName}, mapperProxy);
    }

    @Override
    public Class<?> getObjectType() {
//        try {
//            return Class.forName(innerClassName);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        return innerClassName;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
