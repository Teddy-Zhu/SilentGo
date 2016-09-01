package com.silentgo.core.route.support.annotationResolver;

import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.exception.AppException;
import com.silentgo.core.render.support.ErrorRener;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.kit.ClassKit;
import com.silentgo.kit.CollectionKit;
import com.silentgo.servlet.http.HttpStatus;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;

/**
 * Project : silentgo
 * com.silentgo.core.route.support.annotationResolver
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/30.
 */
public class ParamAnFactory extends BaseFactory {

    private HashMap<Class<? extends Annotation>, ParamAnnotationResolver> resolvers = new HashMap<>();


    public boolean addResolver(ParamAnnotationResolver paramAnnotationResolver) {
        CollectionKit.MapAdd(resolvers, (Class<? extends Annotation>) ClassKit.getGenericClass(paramAnnotationResolver.getClass(), 0), paramAnnotationResolver);
        return true;
    }

    private ParamAnnotationResolver getResolver(Class<? extends Annotation> clz) {
        return resolvers.get(clz);
    }

    public boolean resolve(MethodAdviser adviser, Request request, Response response) throws AppException {
        for (Annotation annotation : adviser.getAnnotations()) {
            ParamAnnotationResolver resolver = getResolver(annotation.annotationType());
            if (resolver != null) {
                if (!resolver.validate(adviser, response, request, annotation)) {
                    return false;
                }
            }
        }
        return true;
    }
}
