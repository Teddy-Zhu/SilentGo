package com.silentgo.core.aop;

import com.silentgo.core.aop.annotationintercept.IAnnotation;
import com.silentgo.core.aop.aspect.AspectMethod;
import com.silentgo.kit.CollectionKit;
import net.sf.cglib.reflect.FastMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Project : silentgo
 * com.silentgo.core.aop
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/27.
 */
public class MethodAdviser {

    private String methodName;

    private String className;

    private FastMethod method;

    private MethodParam[] params;

    private List<Class<? extends Annotation>> anTypes;

    private List<Annotation> annotations = new ArrayList<>();

    public String getMethodName() {
        return methodName;
    }

    public Method getName() {
        return method.getJavaMethod();
    }

    public MethodParam[] getParams() {
        return params;
    }

    public MethodAdviser(String className, String name, FastMethod method, MethodParam[] params, List<Annotation> annotations) {
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

    public FastMethod getMethod() {
        return method;
    }
}
