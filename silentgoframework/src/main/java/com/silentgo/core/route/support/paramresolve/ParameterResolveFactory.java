package com.silentgo.core.route.support.paramresolve;

import com.silentgo.core.config.Const;
import com.silentgo.core.route.ParameterDispatcher;
import com.silentgo.core.route.ParameterValueResolver;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.kit.ClassKit;
import com.silentgo.kit.CollectionKit;

import java.util.HashMap;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.route.support.paramresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/22.
 */
public class ParameterResolveFactory implements BaseFactory {

    private ParameterValueResolver defaultResolver = new CommomParamResolver();

    private Map<Class<?>, ParameterValueResolver> parameterValueResolverMap = new HashMap<>();

    public ParameterValueResolver getParameterValueResolver(Class<?> clz) {
        return parameterValueResolverMap.getOrDefault(clz, defaultResolver);
    }

    public boolean addParameterResolver(ParameterValueResolver resolver) {
        return CollectionKit.MapAdd(parameterValueResolverMap, ClassKit.getGenericClass(resolver, 0), resolver);
    }

}
