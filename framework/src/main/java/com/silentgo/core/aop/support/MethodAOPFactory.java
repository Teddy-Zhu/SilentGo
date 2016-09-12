package com.silentgo.core.aop.support;

import com.silentgo.core.aop.Interceptor;
import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.utils.CollectionKit;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.aop.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/16.
 */
public class MethodAOPFactory extends BaseFactory {
    private Map<Method, MethodAdviser> methodAdviserMap = new HashMap<>();

    private Map<Method, List<Interceptor>> buildedMethodInterceptors = new HashMap<>();

    public boolean addMethodAdviser(MethodAdviser methodAdviser) {
        return CollectionKit.MapAdd(methodAdviserMap, methodAdviser.getName(), methodAdviser);
    }

    public Map<Method, MethodAdviser> getMethodAdviserMap() {
        return methodAdviserMap;
    }

    public List<Interceptor> getBuildedMethodInterceptors(Method method) {
        return buildedMethodInterceptors.get(method);
    }

    public boolean addBuildedInterceptor(Method name, List<Interceptor> interceptors) {
        return CollectionKit.MapAdd(buildedMethodInterceptors, name, interceptors);
    }

    public MethodAdviser getMethodAdviser(Method method) {
        return methodAdviserMap.get(method);
    }

}
