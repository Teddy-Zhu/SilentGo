package com.silentgo.core.aop.annotationintercept;

import com.silentgo.core.aop.AOPPoint;

import java.lang.annotation.Annotation;

/**
 * Project : silentgo
 * com.silentgo.core.aop.annotationintercept
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/27.
 */
public interface IAnnotation<T extends Annotation> {
    default int priority() {
        return Integer.MIN_VALUE;
    }

    Object intercept(AOPPoint point, boolean[] isResolved, T annotation) throws Throwable;

}
