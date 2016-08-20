package com.silentgo.core.aop.aspect.support;

import com.silentgo.build.SilentGoBuilder;
import com.silentgo.build.annotation.Builder;
import com.silentgo.config.Const;
import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.annotation.Around;
import com.silentgo.core.aop.annotation.Aspect;
import com.silentgo.core.aop.aspect.AspectMethod;
import com.silentgo.core.aop.support.MethodAOPFactory;
import com.silentgo.core.ioc.bean.BeanFactory;
import com.silentgo.core.ioc.bean.BeanWrapper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Project : silentgo
 * com.silentgo.core.aop.aspect.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/16.
 */
@Builder
public class AspectBuilder extends SilentGoBuilder {

    @Override
    public Integer priority() {
        return 35;
    }

    @Override
    public boolean build(SilentGo me) {
        BeanFactory beanFactory = (BeanFactory) me.getConfig().getFactory(Const.BeanFactory);
        AspectFactory aspectFactory = new AspectFactory();

        me.getConfig().addFactory(aspectFactory);

        me.getAnnotationManager().getClasses(Aspect.class).forEach(aClass -> {

            BeanWrapper beanDefinition = beanFactory.getBean(aClass.getName());

            Method[] methods = aClass.getDeclaredMethods();
            for (Method method : methods) {
                Around annotation = method.getAnnotation(Around.class);
                if (annotation == null) continue;
                aspectFactory.addAspectMethod(new AspectMethod(annotation.value()
                        , annotation.regex()
                        , beanDefinition.getBeanClass().getMethod(method)
                        , beanDefinition.getBean()
                ));
            }
        });
        MethodAOPFactory methodAOPFactory = (MethodAOPFactory) me.getConfig().getFactory(Const.MethodAOPFactory);
        List<String> methodNames = new ArrayList<>(methodAOPFactory.getMethodAdviserMap().keySet());
        //build aspect
        aspectFactory.getAspectMethods().forEach(aspectMethod -> {
            if (aspectMethod.isRegex()) {
                methodNames.forEach(name -> {
                    if (name.matches(aspectMethod.getRule()))
                        aspectFactory.addAspectMethodInMap(name, aspectMethod);
                });
            } else {
                if (methodNames.contains(aspectMethod.getRule())) {
                    aspectFactory.addAspectMethodInMap(aspectMethod.getRule(), aspectMethod);
                }
            }
        });
        return true;
    }
}
