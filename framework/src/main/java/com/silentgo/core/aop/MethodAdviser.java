package com.silentgo.core.aop;

import com.silentgo.utils.reflect.SGMethod;
import com.silentgo.utils.reflect.SGParameter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Project : silentgo
 * com.silentgo.core.aop
 * <p>
 * <p>
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         Created by teddyzhu on 16/7/27.
 */
public class MethodAdviser {

    private SGMethod sgMethod;

    private MethodParam[] params;

    private List<Annotation> annotations = new ArrayList<>();

    public String getMethodName() {
        return sgMethod.getFullName();
    }

    public Method getName() {
        return sgMethod.getMethod();
    }

    public MethodParam[] getParams() {
        return params;
    }

    public MethodAdviser(SGMethod sgMethod) {
        this.sgMethod = sgMethod;
        this.params = new MethodParam[sgMethod.getParameterNames().length];
        for (int i = 0; i < sgMethod.getParameterNames().length; i++) {
            this.params[i] = new MethodParam(sgMethod.getParameterMap().get(sgMethod.getParameterNames()[i]));
        }
        this.annotations = new ArrayList<>(sgMethod.getAnnotationMap().values());
    }

    public String getClassName() {
        return sgMethod.getClassName();
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public boolean existAnnotation(Class<? extends Annotation> clz) {
        return sgMethod.getAnnotationMap().containsKey(clz);
    }

    @SuppressWarnings("unchecked")
    public <T extends Annotation> T getAnnotation(Class<T> tClass) {
        return (T) sgMethod.getAnnotationMap().get(tClass);
    }

    public Method getMethod() {
        return sgMethod.getMethod();
    }
}
