package com.yy.vokiller;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Set;

public class VOScanner extends ClassPathBeanDefinitionScanner {


    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }


    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

        if (beanDefinitions.isEmpty()) {
            System.out.println("no mapper");
        } else {
            processBeanDefinitions(beanDefinitions);
        }

        return beanDefinitions;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();
            definition.setBeanClass(MapperFactoryBean.class);

        }
    }


    public VOScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    class MapperFactoryBean<T> implements FactoryBean<T> {
        private MapperProxy mapperProxy = new MapperProxy();
        private Class<T> mapperInterface;

        public T getObject() throws Exception {
            return (T) Proxy.newProxyInstance(this.mapperInterface.getClassLoader(),
                    new Class[]{this.mapperInterface}, mapperProxy);
        }

        public Class<?> getObjectType() {
            return this.mapperInterface;
        }

        public boolean isSingleton() {
            return true;
        }
    }

    class MapperProxy<T> implements InvocationHandler, Serializable {

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("gogogogogogo");
            return null;
        }
    }
}
