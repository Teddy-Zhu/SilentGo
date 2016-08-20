package com.silentgo.core.aop.aspect.support;

import com.silentgo.config.Const;
import com.silentgo.core.aop.aspect.AspectMethod;
import com.silentgo.core.support.BaseFactory;
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
public class AspectFactory implements BaseFactory {

    private List<AspectMethod> aspectMethods = new ArrayList<>();

    private Map<String, List<AspectMethod>> methodAspectMap = new HashMap<>();

    public List<AspectMethod> getAspectMethods() {
        return aspectMethods;
    }

    public boolean addAspectMethod(AspectMethod aspectMethod) {
        return CollectionKit.ListAdd(aspectMethods, aspectMethod);
    }

    public List<AspectMethod> getAspectMethod(String methodName) {
        return methodAspectMap.get(methodName);
    }

    public boolean addAspectMethodInMap(String methodName, AspectMethod method) {
        CollectionKit.ListMapAdd(methodAspectMap, methodName, method);
        return true;
    }

    @Override
    public String getName() {
        return Const.AspectFactory;
    }
}
