package com.silentgo.core.ioc.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Project : silentgo
 * com.silentgo.core.ioc.bean
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/20.
 */
public class FieldBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(FieldBean.class);

    private String beanName;

    private boolean hasSet = false;

    private boolean hasGet = false;

    private Method setMethod;
    private Method getMethod;

    private Field field;

    public Object getValue(Object source) {
        if (hasGet) {
            getMethod.setAccessible(true);
            try {
                return getMethod.invoke(source);
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOGGER.error("bean field invoke : {}", beanName);
            }
        } else {
            field.setAccessible(true);
            try {
                return field.get(source);
            } catch (IllegalAccessException e) {
                LOGGER.error("bean field get invoke: {}", beanName);
            }
        }
        return null;
    }

    public boolean setValue(Object source, Object target) {
        if (hasSet) {
            setMethod.setAccessible(true);
            try {
                setMethod.invoke(source, target);
                return true;
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOGGER.error("bean field invoke set method failed: {}", beanName);
            }
        } else {
            field.setAccessible(true);
            try {
                field.set(source, target);
                return true;
            } catch (IllegalAccessException e) {
                LOGGER.error("bean field invoke set failed: {}", beanName);
            }
        }

        return false;
    }

    public boolean isHasGet() {
        return hasGet;
    }

    public void setHasGet(boolean hasGet) {
        this.hasGet = hasGet;
    }

    public Method getGetMethod() {
        return getMethod;
    }

    public void setGetMethod(Method getMethod) {
        this.getMethod = getMethod;
    }

    public FieldBean(Field field) {
        this.field = field;
    }

    public boolean isHasSet() {
        return hasSet;
    }

    public void setHasSet(boolean hasSet) {
        this.hasSet = hasSet;
    }

    public Method getSetMethod() {
        return setMethod;
    }

    public void setSetMethod(Method setMethod) {
        this.setMethod = setMethod;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
