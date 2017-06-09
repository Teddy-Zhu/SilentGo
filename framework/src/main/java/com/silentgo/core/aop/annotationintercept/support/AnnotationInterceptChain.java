package com.silentgo.core.aop.annotationintercept.support;

import com.silentgo.core.aop.AOPPoint;
import com.silentgo.core.aop.annotationintercept.IAnnotation;
import com.silentgo.core.ioc.bean.BeanWrapper;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

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

    private List<Map.Entry<Annotation, BeanWrapper>> interceptors;

    private int size = 0;

    public AOPPoint getPoint() {
        return point;
    }

    public AnnotationInterceptChain(AOPPoint point, List<Map.Entry<Annotation, BeanWrapper>> interceptors) {
        this.point = point;
        this.interceptors = interceptors;
        this.size = interceptors.size();
        this.index = 0;
    }

    public Object intercept() throws Throwable {
        Object returnValue = null;
        if (index < size) {
            Map.Entry<Annotation, BeanWrapper> entry = interceptors.get(index++);

            //noinspection unchecked
            returnValue = ((IAnnotation) entry.getValue().getObject()).intercept(this, point.getResponse(), point.getRequest(), entry.getKey());

        } else if (index++ == size) {
            return point.proceed();
        }

        return returnValue;

    }

}
