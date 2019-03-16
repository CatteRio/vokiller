package com.yy.vokiller.core;

import com.yy.vokiller.exception.NoPropertyException;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 *
 * @author Macbook
 */
public class VObject implements Serializable {


    public Object getValue(String property) throws NoPropertyException {
        try{
            Field field = this.getClass().getDeclaredField(property);
            field.setAccessible(true);
            return field.get(this);
        }catch (NoSuchFieldException e){
            throw new NoPropertyException(property);
        }catch (IllegalAccessException e){
            return null;
        }
    }

}
