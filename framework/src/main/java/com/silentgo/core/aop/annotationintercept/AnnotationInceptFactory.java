package com.silentgo.core.aop.annotationintercept;

import com.silentgo.core.aop.validator.support.StringValidator;
import com.silentgo.kit.CollectionKit;

import java.util.*;

/**
 * Project : silentgo
 * com.silentgo.core.aop.annotationintercept
 *
 * @author <Acc href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</Acc>
 *         <p>
 *         Created by teddyzhu on 16/7/27.
 */
public class AnnotationInceptFactory {


    /**
     * Key : Method Name  Value : Sorted AnnotationInterceptor
     */
    private static Map<String, List<IAnnotation>> annotationInterceptorMap = new HashMap<>();


    public static boolean addAnnotationInterceptor(String methodName, IAnnotation interceptor, boolean refresh) {
        List<IAnnotation> annotationInterceptors = CollectionKit.ListMapAdd(annotationInterceptorMap, methodName, interceptor);
        if (refresh && !annotationInterceptors.isEmpty()) {
            annotationInterceptors.sort((o1, o2) -> {
                int x = o1.priority();
                int y = o2.priority();
                return (x < y) ? -1 : ((x == y) ? 0 : 1);
            });
        }
        return true;
    }

    public static List<IAnnotation> getAnnotationInterceptors(String methodName) {
        List<IAnnotation> interceptors = annotationInterceptorMap.get(methodName);

        return interceptors == null || interceptors.isEmpty() ? new ArrayList<>() : interceptors;
    }
}
