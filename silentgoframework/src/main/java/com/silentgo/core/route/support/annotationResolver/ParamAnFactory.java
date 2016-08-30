package com.silentgo.core.route.support.annotationResolver;

import com.silentgo.core.support.BaseFactory;
import com.silentgo.kit.ClassKit;
import com.silentgo.kit.CollectionKit;

import java.lang.annotation.Annotation;
import java.util.HashMap;

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

    public ParamAnnotationResolver getResolver(Class<? extends Annotation> clz) {
        return resolvers.get(clz);
    }
}
