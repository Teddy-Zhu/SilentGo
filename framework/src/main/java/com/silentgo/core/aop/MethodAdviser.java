package com.silentgo.core.aop;

import com.silentgo.core.aop.annotation.Intercept;
import com.silentgo.core.aop.annotationintercept.IAnnotation;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.aop
 *
 * @author <Acc href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</Acc>
 *         <p>
 *         Created by teddyzhu on 16/7/27.
 */
public class MethodAdviser {

    private String name;

    private List<MethodParam> params;

    private Map<Annotation, IAnnotation> annotationMap;

    private List<Interceptor> interceptors;

    public String getName() {
        return name;
    }

    public List<MethodParam> getParams() {
        return params;
    }

    public Map<Annotation, IAnnotation> getAnnotationMap() {
        return annotationMap;
    }

    public MethodAdviser(String name, List<MethodParam> params, Map<Annotation, IAnnotation> annotationMap, List<Interceptor> interceptors) {
        this.name = name;
        this.params = params;
        this.annotationMap = annotationMap;
        this.interceptors = interceptors;
    }

    public boolean hasAnnotation(Class<? extends Annotation> clz) {
        return annotationMap.containsKey(clz);
    }

}
