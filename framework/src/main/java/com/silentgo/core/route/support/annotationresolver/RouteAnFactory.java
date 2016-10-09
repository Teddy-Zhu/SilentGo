package com.silentgo.core.route.support.annotationresolver;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.build.Factory;
import com.silentgo.core.exception.AppBuildException;
import com.silentgo.core.exception.AppException;
import com.silentgo.core.exception.AppReleaseException;
import com.silentgo.core.route.support.annotationresolver.annotation.RouteAnResolver;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;
import com.silentgo.utils.ClassKit;
import com.silentgo.utils.CollectionKit;

import java.lang.annotation.Annotation;
import java.util.HashMap;

/**
 * Project : silentgo
 * com.silentgo.core.route.support.annotationresolver
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/30.
 */
@Factory
public class RouteAnFactory extends BaseFactory {

    private HashMap<Class<? extends Annotation>, RouteAnnotationResolver> resolvers = new HashMap<>();


    public boolean addResolver(RouteAnnotationResolver paramAnnotationResolver) {
        CollectionKit.MapAdd(resolvers, (Class<? extends Annotation>) ClassKit.getGenericClass(paramAnnotationResolver.getClass(), 0), paramAnnotationResolver);
        return true;
    }

    private RouteAnnotationResolver getResolver(Class<? extends Annotation> clz) {
        return resolvers.get(clz);
    }

    public boolean resolve(MethodAdviser adviser, Request request, Response response) throws AppException {
        for (Annotation annotation : adviser.getAnnotations()) {
            RouteAnnotationResolver resolver = getResolver(annotation.annotationType());
            if (resolver != null) {
                if (!resolver.validate(adviser, response, request, annotation)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean initialize(SilentGo me) throws AppBuildException {

        me.getAnnotationManager().getClasses(RouteAnResolver.class).forEach(aClass -> {
            if (RouteAnnotationResolver.class.isAssignableFrom(aClass)) {
                try {
                    addResolver((RouteAnnotationResolver) aClass.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        return true;
    }

    @Override
    public boolean destroy(SilentGo me) throws AppReleaseException {
        return false;
    }
}
