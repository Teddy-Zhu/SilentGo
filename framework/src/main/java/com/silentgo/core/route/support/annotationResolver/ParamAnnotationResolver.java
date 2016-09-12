package com.silentgo.core.route.support.annotationResolver;

import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.exception.AppException;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

import java.lang.annotation.Annotation;

/**
 * Project : silentgo
 * com.silentgo.core.route.support.annotationResolver
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/30.
 */
public interface ParamAnnotationResolver<T extends Annotation> {

    public boolean validate(MethodAdviser adviser, Response response, Request request, T annotaion) throws AppException;
}
