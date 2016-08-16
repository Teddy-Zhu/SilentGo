package com.silentgo.core.aop.aspect.support;

import com.silentgo.build.Builder;
import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.aop.annotation.Around;
import com.silentgo.core.aop.annotation.Aspect;
import com.silentgo.core.aop.aspect.AspectMethod;
import com.silentgo.core.aop.support.MethodAOPBuilder;
import com.silentgo.core.aop.support.MethodAOPFactory;
import com.silentgo.core.ioc.bean.BeanWrapper;

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
 *         Created by teddyzhu on 16/8/16.
 */
public class AspectBuilder extends Builder {
    @Override
    public boolean build(SilentGo me) {
        me.getAnnotationManager().getClasses(Aspect.class).forEach(aClass -> {

            BeanWrapper beanDefinition = me.getConfig().getBeanFactory().getBean(aClass.getName());

            Method[] methods = aClass.getDeclaredMethods();
            for (Method method : methods) {
                Around annotation = method.getAnnotation(Around.class);
                if (annotation == null) continue;
                AspectFactory.addAspectMethod(new AspectMethod(annotation.value()
                        , annotation.regex()
                        , beanDefinition.getBeanClass().getMethod(method)
                        , beanDefinition.getBean()
                ));
            }
        });

        List<String> methodNames = new ArrayList<>(MethodAOPFactory.getMethodAdviserMap().keySet());
        //build aspect
        AspectFactory.getAspectMethods().forEach(aspectMethod -> {
            if (aspectMethod.isRegex()) {
                methodNames.forEach(name -> {
                    if (name.matches(aspectMethod.getRule()))
                        AspectFactory.addAspectMethodInMap(name, aspectMethod);
                });
            } else {
                if (methodNames.contains(aspectMethod.getRule())) {
                    AspectFactory.addAspectMethodInMap(aspectMethod.getRule(), aspectMethod);
                }
            }
        });
        return true;
    }
}
