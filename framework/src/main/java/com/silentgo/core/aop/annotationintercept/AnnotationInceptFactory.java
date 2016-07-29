package com.silentgo.core.aop.annotationintercept;

import com.silentgo.core.aop.validator.support.StringValidator;
import com.silentgo.kit.CollectionKit;

import java.lang.annotation.Annotation;
import java.util.*;

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


    public static boolean addAnnotationInterceptor(Class<? extends Annotation> clz, IAnnotation interceptor) {
        return CollectionKit.MapAdd(annotationInterceptorMap, clz, interceptor);
    }

    public static IAnnotation getAnnotationInterceptor(Class<? extends Annotation> clz) {
        return annotationInterceptorMap.get(clz);
    }

}
