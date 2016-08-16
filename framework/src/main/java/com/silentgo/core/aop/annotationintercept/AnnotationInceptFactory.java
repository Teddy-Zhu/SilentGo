package com.silentgo.core.aop.annotationintercept;

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
public class AnnotationInceptFactory {


    /**
     * Key : Method Name  Value : Sorted AnnotationInterceptor
     */
    private static Map<Class<? extends Annotation>, IAnnotation> annotationInterceptorMap = new HashMap<>();

    private static Map<String, Map<Annotation, IAnnotation>> iAnnotationMap = new HashMap<>();

    private static Map<String, List<Map.Entry<Annotation, IAnnotation>>> sortedIAnnotationMap = new HashMap<>();


    public static boolean addAnnotationInterceptor(Class<? extends Annotation> clz, IAnnotation interceptor) {
        return CollectionKit.MapAdd(annotationInterceptorMap, clz, interceptor);
    }

    public static IAnnotation getAnnotationInterceptor(Class<? extends Annotation> clz) {
        return annotationInterceptorMap.get(clz);
    }

    public static boolean addIAnnotation(String methodName, Map<Annotation, IAnnotation> anMap) {
        CollectionKit.MapAdd(iAnnotationMap, methodName, anMap);
        return true;
    }
    public static List<Map.Entry<Annotation, IAnnotation>> getSortedAnnotationMap(String methodName) {
        return sortedIAnnotationMap.get(methodName);
    }

    public static Map<Annotation, IAnnotation> getAnnotationMap(String methodName) {
        return iAnnotationMap.get(methodName);
    }

    public static boolean addSortedIAnnotation(String methodName, List<Map.Entry<Annotation, IAnnotation>> iAnnotationMap) {
        return CollectionKit.MapAdd(sortedIAnnotationMap, methodName, iAnnotationMap);
    }
}
