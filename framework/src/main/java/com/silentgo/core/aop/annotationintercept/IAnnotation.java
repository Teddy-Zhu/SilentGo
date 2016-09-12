package com.silentgo.core.aop.annotationintercept;

import com.silentgo.core.aop.annotationintercept.support.AnnotationInterceptChain;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

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

    Object intercept(AnnotationInterceptChain chain, Response response, Request request, T annotation) throws Throwable;

}
