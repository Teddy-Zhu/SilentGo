package com.silentgo.core.aop.aspect.support;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.annotation.Around;
import com.silentgo.core.aop.annotation.Aspect;
import com.silentgo.core.aop.aspect.AspectMethod;
import com.silentgo.core.aop.support.MethodAOPFactory;
import com.silentgo.core.build.Factory;
import com.silentgo.core.exception.AppBuildException;
import com.silentgo.core.exception.AppReleaseException;
import com.silentgo.core.ioc.bean.BeanFactory;
import com.silentgo.core.ioc.bean.BeanWrapper;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.utils.CollectionKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(AspectFactory.class);

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

    @Override
    public boolean initialize(SilentGo me) throws AppBuildException {
        BeanFactory beanFactory = me.getFactory(me.getConfig().getBeanClass());

        me.getAnnotationManager().getClasses(Aspect.class).forEach(aClass -> {

            BeanWrapper beanDefinition = beanFactory.getBean(aClass.getName());

            Method[] methods = aClass.getDeclaredMethods();
            for (Method method : methods) {
                Around annotation = method.getAnnotation(Around.class);
                if (annotation == null) continue;
                addAspectMethod(new AspectMethod(annotation.value()
                        , annotation.regex()
                        , method
                        , beanDefinition.getObject()
                ));
            }
        });
        MethodAOPFactory methodAOPFactory = me.getFactory(MethodAOPFactory.class);
        List<String> methodNames = new ArrayList<>();
        methodAOPFactory.getMethodAdviserMap().forEach((k, v) -> {
            CollectionKit.ListAdd(methodNames, v.getMethodName());
        });

        //build aspect
        aspectMethods.forEach(aspectMethod -> {
            if (aspectMethod.getMethod().getParameterCount() != 1) {
                LOGGER.warn("The Method [{}] ignored .", aspectMethod.getMethod().getName());
                return;
            }

            if (aspectMethod.isRegex()) {
                methodNames.forEach(name -> {
                    if (name.matches(aspectMethod.getRule())) {
                        addAspectMethod(methodAOPFactory, aspectMethod, name);
                    }
                });
            } else {
                if (methodNames.contains(aspectMethod.getRule())) {
                    addAspectMethod(methodAOPFactory, aspectMethod, aspectMethod.getRule());
                }
            }
        });
        return true;
    }

    @Override
    public boolean destroy(SilentGo me) throws AppReleaseException {
        return false;
    }


    private void addAspectMethod(MethodAOPFactory methodAOPFactory, AspectMethod aspectMethod, String methodName) {
        methodAOPFactory.getMethodAdviserMap().forEach((k, v) -> {
            if (v.getMethodName().equals(methodName)) {
                addAspectMethodInMap(v.getName(), aspectMethod);
            }
        });
    }
}
