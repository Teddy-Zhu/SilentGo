package com.silentgo.utils.reflect;

import java.lang.reflect.Field;

/**
 * Project : SilentGo
 * Package : com.silentgo.utils.reflect
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2017/1/5.
 */
public class SGField extends AnnotaionMap {

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
}
