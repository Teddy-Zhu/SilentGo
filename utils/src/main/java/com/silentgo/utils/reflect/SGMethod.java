package com.silentgo.utils.reflect;

import com.silentgo.utils.exception.UtilException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Project : SilentGo
 * Package : com.silentgo.utils.reflect
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2017/1/5.
 */
public class SGMethod extends ParameterAnnotationMap {

    private String name;

    private String className;

    private String fullName;

    private Method method;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Object invoke(Object target, Object... objects) {
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        try {
            return method.invoke(target, objects);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new UtilException("invoke method error", e);
        }
    }
}
