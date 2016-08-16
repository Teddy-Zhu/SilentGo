package com.silentgo.core.aop.annotationintercept;

import com.silentgo.core.aop.AOPPoint;
import com.silentgo.core.aop.Interceptor;
import com.silentgo.core.aop.support.InterceptChain;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Project : silentgo
 * com.silentgo.core.aop.annotationintercept
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/28.
 */
public class AnnotationInterceptChain {
    private int index;

    private AOPPoint point;

    private boolean[] isResolved;

    private List<Map.Entry<Annotation, IAnnotation>> interceptors;

    private int size = 0;

    public AnnotationInterceptChain(AOPPoint point, boolean[] isResolved, List<Map.Entry<Annotation, IAnnotation>> interceptors) {
        this.point = point;
        this.isResolved = isResolved;
        this.interceptors = interceptors;
        this.size = interceptors.size();
        this.index = 0;
    }

    public Object intercept() throws Throwable {
        Object returnValue = null;
        if (index < size) {
            Map.Entry<Annotation, IAnnotation> entry = interceptors.get(index++);

            //noinspection unchecked
            returnValue = entry.getValue().intercept(this, point.getResponse(), point.getRequest(), isResolved, entry.getKey());

            if (isResolved[0]) {
                return returnValue;
            }
        } else if (index++ == size) {
            return point.doChain();
        }

        return returnValue;

    }

}
