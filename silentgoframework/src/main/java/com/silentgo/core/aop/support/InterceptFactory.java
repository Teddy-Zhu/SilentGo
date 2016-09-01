package com.silentgo.core.aop.support;

import com.silentgo.core.config.Const;
import com.silentgo.core.aop.Interceptor;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.kit.CollectionKit;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.aop.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/3.
 */
public class InterceptFactory extends BaseFactory {

    private Map<Class<? extends Interceptor>, Interceptor> allInterceptors = new HashMap<>();

    private Map<String, List<Interceptor>> classInterceptors = new HashMap<>();


    private Map<Method, List<Interceptor>> methodInterceptors = new HashMap<>();


    public Map<Class<? extends Interceptor>, Interceptor> getAllInterceptors() {
        return allInterceptors;
    }

    public Map<String, List<Interceptor>> getClassInterceptors() {
        return classInterceptors;
    }

    public Map<Method, List<Interceptor>> getMethodInterceptors() {
        return methodInterceptors;
    }

    public boolean addAllInterceltor(Class<? extends Interceptor> name) throws IllegalAccessException, InstantiationException {
        return CollectionKit.MapAdd(allInterceptors, name, name.newInstance());
    }

    public boolean addAllInterceltor(List<Interceptor> interceptors) {
        interceptors.forEach(interceptor -> {
            CollectionKit.MapAdd(allInterceptors, interceptor.getClass(), interceptor);
        });
        return true;
    }

    public boolean addAllInterceltor(Class<? extends Interceptor> name, Interceptor interceptor) {
        return CollectionKit.MapAdd(allInterceptors, name, interceptor);
    }

    public boolean addMethodInterceptor(Method name, Interceptor interceptor) {
        CollectionKit.ListMapAdd(methodInterceptors, name, interceptor);
        return true;
    }

    public boolean addClassInterceptor(String name, Interceptor interceptor) {
        CollectionKit.ListMapAdd(classInterceptors, name, interceptor);
        return true;
    }

    public boolean addClassInterceptor(String name, List<Interceptor> interceptors) {
        CollectionKit.ListMapAdd(classInterceptors, name, interceptors);
        return true;
    }

}
