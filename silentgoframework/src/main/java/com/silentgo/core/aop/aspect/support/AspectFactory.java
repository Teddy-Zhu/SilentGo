package com.silentgo.core.aop.aspect.support;

import com.silentgo.core.config.Const;
import com.silentgo.core.aop.aspect.AspectMethod;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.kit.CollectionKit;

import java.lang.reflect.Method;
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
public class AspectFactory extends BaseFactory {

    private List<AspectMethod> aspectMethods = new ArrayList<>();

    private Map<Method, List<AspectMethod>> methodAspectMap = new HashMap<>();

    public List<AspectMethod> getAspectMethods() {
        return aspectMethods;
    }

    public boolean addAspectMethod(AspectMethod aspectMethod) {
        return CollectionKit.ListAdd(aspectMethods, aspectMethod);
    }

    public List<AspectMethod> getAspectMethod(Method name) {
        return methodAspectMap.get(name);
    }

    public boolean addAspectMethodInMap(Method name, AspectMethod method) {
        CollectionKit.ListMapAdd(methodAspectMap, name, method);
        return true;
    }

}
