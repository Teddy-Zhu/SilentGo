package com.silentgo.core.aop.annotationintercept.support;

import com.silentgo.core.config.Const;
import com.silentgo.core.aop.annotationintercept.IAnnotation;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.kit.CollectionKit;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.aop.annotationintercept
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/27.
 */
public class AnnotationInceptFactory implements BaseFactory {


    /**
     * Key : Method Name  Value : Sorted AnnotationInterceptor
     */
    private Map<Class<? extends Annotation>, IAnnotation> annotationInterceptorMap = new HashMap<>();

    private Map<String, Map<Annotation, IAnnotation>> iAnnotationMap = new HashMap<>();

    private Map<String, List<Map.Entry<Annotation, IAnnotation>>> sortedIAnnotationMap = new HashMap<>();


    public boolean addAnnotationInterceptor(Class<? extends Annotation> clz, IAnnotation interceptor) {
        return CollectionKit.MapAdd(annotationInterceptorMap, clz, interceptor);
    }

    public IAnnotation getAnnotationInterceptor(Class<? extends Annotation> clz) {
        return annotationInterceptorMap.get(clz);
    }

    public boolean addIAnnotation(String methodName, Map<Annotation, IAnnotation> anMap) {
        CollectionKit.MapAdd(iAnnotationMap, methodName, anMap);
        return true;
    }

    public List<Map.Entry<Annotation, IAnnotation>> getSortedAnnotationMap(String methodName) {
        return sortedIAnnotationMap.get(methodName);
    }

    public Map<Annotation, IAnnotation> getAnnotationMap(String methodName) {
        return iAnnotationMap.get(methodName);
    }

    public boolean addSortedIAnnotation(String methodName, List<Map.Entry<Annotation, IAnnotation>> iAnnotationMap) {
        return CollectionKit.MapAdd(sortedIAnnotationMap, methodName, iAnnotationMap);
    }
}
