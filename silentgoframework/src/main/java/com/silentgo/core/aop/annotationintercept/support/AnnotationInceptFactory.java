package com.silentgo.core.aop.annotationintercept.support;

import com.silentgo.core.config.Const;
import com.silentgo.core.aop.annotationintercept.IAnnotation;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.kit.CollectionKit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
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
public class AnnotationInceptFactory extends BaseFactory {


    /**
     * Key : Method Name  Value : Sorted AnnotationInterceptor
     */
    private Map<Class<? extends Annotation>, IAnnotation> annotationInterceptorMap = new HashMap<>();

    private Map<Method, Map<Annotation, IAnnotation>> iAnnotationMap = new HashMap<>();

    private Map<Method, List<Map.Entry<Annotation, IAnnotation>>> sortedIAnnotationMap = new HashMap<>();


    public boolean addAnnotationInterceptor(Class<? extends Annotation> clz, IAnnotation interceptor) {
        return CollectionKit.MapAdd(annotationInterceptorMap, clz, interceptor);
    }

    public IAnnotation getAnnotationInterceptor(Class<? extends Annotation> clz) {
        return annotationInterceptorMap.get(clz);
    }

    public boolean addIAnnotation(Method name, Map<Annotation, IAnnotation> anMap) {
        CollectionKit.MapAdd(iAnnotationMap, name, anMap);
        return true;
    }

    public List<Map.Entry<Annotation, IAnnotation>> getSortedAnnotationMap(Method name) {
        return sortedIAnnotationMap.get(name);
    }

    public Map<Annotation, IAnnotation> getAnnotationMap(Method name) {
        return iAnnotationMap.get(name);
    }

    public boolean addSortedIAnnotation(Method name, List<Map.Entry<Annotation, IAnnotation>> iAnnotationMap) {
        return CollectionKit.MapAdd(sortedIAnnotationMap, name, iAnnotationMap);
    }
}
