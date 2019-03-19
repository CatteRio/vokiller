package com.yy.vokiller.utils;

import org.omg.CORBA.OBJECT_NOT_EXIST;

import java.util.Map;

/**
 * @author yuanyang(417168602 @ qq.com)
 * @date 2019/3/18 23:47
 */
public class AnnotationEntry<K, V> implements Map.Entry<K, V> {

    private Integer position;
    private K key;
    private V value;

    @Override
    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        V tmp = getValue();
        this.value = value;
        return tmp;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}
