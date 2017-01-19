package com.silentgo.utils.reflect;

import com.silentgo.utils.exception.UtilException;

import java.lang.reflect.Field;

/**
 * Project : SilentGo
 * Package : com.silentgo.utils.reflect
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2017/1/5.
 */
public class SGField extends AnnotationMap {

    private SGMethod getMethod;
    private SGMethod setMethod;

    private Class<?> type;

    private Field field;

    public SGMethod getGetMethod() {
        return getMethod;
    }

    public void setGetMethod(SGMethod getMethod) {
        this.getMethod = getMethod;
    }

    public SGMethod getSetMethod() {
        return setMethod;
    }

    public void setSetMethod(SGMethod setMethod) {
        this.setMethod = setMethod;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public void set(Object target, Object value) {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            throw new UtilException("invoke set field : " + field.getName() + " failed", e);
        }
    }

    public Object get(Object target) {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        try {
            return field.get(target);
        } catch (IllegalAccessException e) {
            throw new UtilException("invoke get field : " + field.getName() + " failed", e);
        }
    }

    public void invokeSetMethod(Object target, Object... values) {
        setMethod.invoke(target, values);
    }

    public Object invokeGetMethod(Object target, Object... values) {
        return getMethod.invoke(target, values);
    }

    public boolean hasSet() {
        return field != null || setMethod != null;
    }

    public boolean hasGet() {
        return field != null || getMethod != null;
    }
}
