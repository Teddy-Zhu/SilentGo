package com.silentgo.core.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Project : silentgo
 * com.silentgo.core.aop
 *
 *         <p>
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         Created by teddyzhu on 16/7/27.
 */
public class MethodAdviser {

    private String methodName;

    private String className;

    private Method method;

    private MethodParam[] params;

    private List<Class<? extends Annotation>> anTypes = new ArrayList<>();

    private List<Annotation> annotations = new ArrayList<>();

    public String getMethodName() {
        return methodName;
    }

    public Method getName() {
        return method;
    }

    public MethodParam[] getParams() {
        return params;
    }

    public MethodAdviser(String className, String name, Method method, MethodParam[] params, List<Annotation> annotations) {
        this.methodName = name;
        this.className = className;
        this.method = method;
        this.params = params;
        this.annotations = annotations;
        anTypes = new ArrayList<>();
        annotations.forEach(annotation -> anTypes.add(annotation.annotationType()));
    }

    public String getClassName() {
        return className;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public boolean existAnnotation(Class<? extends Annotation> clz) {
        return anTypes.contains(clz);
    }

    @SuppressWarnings("unchecked")
    public <T extends Annotation> T getAnnotation(Class<T> tClass) {
        Optional optional = annotations.stream().filter(annotation -> annotation.annotationType().equals(tClass)).findFirst();
        if (optional.isPresent()) return (T) optional.get();
        else return null;
    }

    public Method getMethod() {
        return method;
    }
}
