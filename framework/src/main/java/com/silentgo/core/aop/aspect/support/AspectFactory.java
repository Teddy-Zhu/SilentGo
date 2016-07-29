package com.silentgo.core.aop.aspect.support;

import com.silentgo.core.aop.aspect.AspectMethod;
import com.silentgo.kit.CollectionKit;

import java.util.ArrayList;
import java.util.List;

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

    public static List<AspectMethod> getAspectMethods() {
        return aspectMethods;
    }

    public static boolean addAspectMethod(AspectMethod aspectMethod) {
        return CollectionKit.ListAdd(aspectMethods, aspectMethod);
    }
}
