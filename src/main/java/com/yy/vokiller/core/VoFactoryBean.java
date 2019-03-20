package com.yy.vokiller.core;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

/**
 * @author: yuanyang(417168602 @ qq.com)
 * @date: 2019-03-15 20:01
 **/
public class VoFactoryBean<T> implements FactoryBean<T> {
    private Class innerClass;
    public VoFactoryBean(Class<T> mapperInterface) {
        this.innerClass = mapperInterface;
    }
    private VoProxy mapperProxy = new VoProxy();
    @Override
    public T getObject() throws Exception {
        return (T) Proxy.newProxyInstance(innerClass.getClassLoader(), new Class[]{innerClass}, mapperProxy);
    }
    @Override
    public Class<?> getObjectType() {
        return innerClass;
    }
    @Override
    public boolean isSingleton() {
        return true;
    }
}
