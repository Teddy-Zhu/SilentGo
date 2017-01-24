package com.silentgo.core.ioc.bean;

import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;
import com.silentgo.utils.reflect.SGField;

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

    private static final Log LOGGER = LogFactory.get();

    private String beanName;

    private SGField field;

    public Object getValue(Object source) {
        if (field.getGetMethod() != null) {
            field.getGetMethod().getMethod().setAccessible(true);
            try {
                return field.getGetMethod().getMethod().invoke(source);
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOGGER.error("bean field invoke : {}", beanName);
            }
        } else {
            field.getField().setAccessible(true);
            try {
                return field.getField().get(source);
            } catch (IllegalAccessException e) {
                LOGGER.error("bean field get invoke: {}", beanName);
            }
        }
        return null;
    }

    public boolean setValue(Object source, Object target) {
        if (field.getSetMethod() != null) {
            field.getSetMethod().getMethod().setAccessible(true);
            try {
                field.getSetMethod().getMethod().invoke(source, target);
                return true;
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOGGER.error("bean field invoke set method failed: {}", beanName);
            }
        } else {
            field.getField().setAccessible(true);
            try {
                field.getField().set(source, target);
                return true;
            } catch (IllegalAccessException e) {
                LOGGER.error("bean field invoke set failed: {}", beanName);
            }
        }

        return false;
    }


    public FieldBean(SGField field, String beanName) {
        this.field = field;
        this.beanName = beanName;
    }

    public Field getField() {
        return field.getField();
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
