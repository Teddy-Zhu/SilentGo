package com.silentgo.utils.reflect;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Project : SilentGo
 * Package : com.silentgo.utils.reflect
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2017/1/5.
 */
public class SGClass extends AnnotaionMap {

    private Class<?> clz;

    private Map<String, SGField> fieldMap;

    private Map<Method, SGMethod> methodMap;

    private SGConstructor defaultConstructor;

    private List<SGConstructor> constructors;

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }

    public Map<String, SGField> getFieldMap() {
        return fieldMap;
    }

    public void setFieldMap(Map<String, SGField> fieldMap) {
        this.fieldMap = fieldMap;
    }

    public Map<Method, SGMethod> getMethodMap() {
        return methodMap;
    }

    public void setMethodMap(Map<Method, SGMethod> methodMap) {
        this.methodMap = methodMap;
    }

    public SGConstructor getDefaultConstructor() {
        return defaultConstructor;
    }

    public void setDefaultConstructor(SGConstructor defaultConstructor) {
        this.defaultConstructor = defaultConstructor;
    }

    public List<SGConstructor> getConstructors() {
        return constructors;
    }

    public void setConstructors(List<SGConstructor> constructors) {
        this.constructors = constructors;
    }

}
