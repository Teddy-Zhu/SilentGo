package com.silentgo.core.aop.aspect.support;

import com.silentgo.core.aop.aspect.AspectMethod;
import com.silentgo.kit.CollectionKit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.aop.aspect.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/29.
 */
public class AspectFactory {

    private static final List<AspectMethod> aspectMethods = new ArrayList<>();

    private static Map<String, List<AspectMethod>> methodAspectMap = new HashMap<>();

    public static List<AspectMethod> getAspectMethods() {
        return aspectMethods;
    }

    public static boolean addAspectMethod(AspectMethod aspectMethod) {
        return CollectionKit.ListAdd(aspectMethods, aspectMethod);
    }

    public static List<AspectMethod> getAspectMethod(String methodName) {
        return methodAspectMap.get(methodName);
    }

    public static boolean addAspectMethodInMap(String methodName, AspectMethod method) {
        CollectionKit.ListMapAdd(methodAspectMap, methodName, method);
        return true;
    }
}
