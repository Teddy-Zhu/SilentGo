package com.silentgo.core.aop.aspect.support;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.annotation.Around;
import com.silentgo.core.aop.annotation.Aspect;
import com.silentgo.core.aop.aspect.AspectMethod;
import com.silentgo.core.build.Factory;
import com.silentgo.core.exception.AppBuildException;
import com.silentgo.core.exception.AppReleaseException;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.utils.CollectionKit;
import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;

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
@Factory
public class AspectFactory extends BaseFactory {

    private static final Log LOGGER = LogFactory.get();

    private List<AspectMethod> aspectMethods = new ArrayList<>();

    private Map<Method, List<AspectMethod>> methodAspectMap = new HashMap<>();

    public List<AspectMethod> getAspectMethods() {
        return aspectMethods;
    }

    public boolean addAspectMethod(AspectMethod aspectMethod) {
        return CollectionKit.ListAdd(aspectMethods, aspectMethod);
    }

    public List<AspectMethod> getAspectMethod(Method name) {
        return methodAspectMap.getOrDefault(name, new ArrayList<>());
    }

    public boolean addAspectMethodInMap(Method name, AspectMethod method) {
        CollectionKit.ListMapAdd(methodAspectMap, name, method);
        return true;
    }

    @Override
    public boolean initialize(SilentGo me) throws AppBuildException {

        me.getAnnotationManager().getClasses(Aspect.class).forEach(aClass -> {

            Method[] methods = aClass.getDeclaredMethods();
            for (Method method : methods) {
                Around annotation = method.getAnnotation(Around.class);
                if (annotation == null) continue;
                addAspectMethod(new AspectMethod(annotation.value()
                        , annotation.regex()
                        , method
                        , aClass
                ));
            }
        });

        return true;
    }

    @Override
    public boolean destroy(SilentGo me) throws AppReleaseException {
        return false;
    }
}
