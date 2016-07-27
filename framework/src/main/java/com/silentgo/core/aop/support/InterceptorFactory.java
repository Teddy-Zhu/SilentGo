package com.silentgo.core.aop.support;

import com.silentgo.core.aop.Interceptor;
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
 *         Created by teddyzhu on 16/7/27.
 */
public class InterceptorFactory {

    private static Map<String, List<Interceptor>> classInterceptorMap = new HashMap<>();

    private static Map<String, List<Interceptor>> methodInterceptorMap = new HashMap<>();

    public static boolean addClassInterceptor(Class<?> clz, Interceptor interceptor) {
        return addClassInterceptor(clz.getName(), interceptor);
    }

    public static boolean addClassInterceptor(String className, Interceptor interceptor) {
        CollectionKit.ListMapAdd(classInterceptorMap, className, interceptor);
        return true;
    }
}
