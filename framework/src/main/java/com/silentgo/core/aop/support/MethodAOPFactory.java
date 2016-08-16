package com.silentgo.core.aop.support;

import com.silentgo.core.aop.Interceptor;
import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.aop.annotation.Intercept;
import com.silentgo.kit.CollectionKit;

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
public class MethodAOPFactory {
    private static final Map<String, MethodAdviser> methodAdviserMap = new HashMap<>();

    private static final Map<String, List<Interceptor>> buildedMethodInterceptors = new HashMap<>();

    public static boolean addMethodAdviser(MethodAdviser methodAdviser) {
        return CollectionKit.MapAdd(methodAdviserMap, methodAdviser.getName(), methodAdviser);
    }

    public static Map<String, MethodAdviser> getMethodAdviserMap() {
        return methodAdviserMap;
    }

    public static List<Interceptor> getBuildedMethodInterceptors(String methodName) {
        return buildedMethodInterceptors.get(methodName);
    }

    public static boolean addBuildedInterceptor(String name, List<Interceptor> interceptors) {
        return CollectionKit.MapAdd(buildedMethodInterceptors, name, interceptors);
    }

    public static MethodAdviser getMethodAdviser(String methodName) {
        return methodAdviserMap.get(methodName);
    }

}
