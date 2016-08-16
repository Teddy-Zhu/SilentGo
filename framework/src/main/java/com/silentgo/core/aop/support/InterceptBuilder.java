package com.silentgo.core.aop.support;

import com.silentgo.build.Builder;
import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.Interceptor;
import com.silentgo.core.aop.annotation.Intercept;
import com.silentgo.core.ioc.bean.BeanDefinition;
import com.silentgo.kit.CollectionKit;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.aop.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/28.
 */
public class InterceptBuilder extends Builder {

    private static boolean addIntercept(List<Interceptor> interceptors, Class<? extends Interceptor> clz) {
        if (interceptors.stream().filter(interceptor -> interceptor.getClass().equals(clz)).count() > 0)
            return false;
        try {
            return interceptors.add(clz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean build(SilentGo me) {

        //build global interceptors
        List<Interceptor> globalInterceptors = me.getConfig().getInterceptors();
        me.getAnnotationManager().getClasses(Intercept.class).forEach(interceptClass -> {
            Intercept intercept = (Intercept) interceptClass.getAnnotation(Intercept.class);
            if (intercept.value().length == 0 &&
                    Interceptor.class.isAssignableFrom(interceptClass)) {
                addIntercept(globalInterceptors, interceptClass);
            } else if (intercept.value().length > 0) {
                for (Class<? extends Interceptor> aClass : intercept.value()) {
                    addIntercept(globalInterceptors, aClass);
                }
            }
        });

        Map<String, BeanDefinition> beanDefinitionMap = (Map<String, BeanDefinition>) me.getConfig().getBeanFactory().getBeans();
        beanDefinitionMap.forEach((k, v) -> {
            InterceptFactory.addClassInterceptor(v.getClassName(), me.getConfig().getInterceptors());
            Method[] methods = v.getSourceClass().getMethods();
            for (Method method : methods) {
                Intercept intercept = method.getAnnotation(Intercept.class);
                if (intercept != null && intercept.value().length != 0) {
                    String methodName = v.getSourceClass().getName() + "." + method.getName();
                    for (Class<? extends Interceptor> aClass : intercept.value()) {
                        InterceptFactory.addMethodInterceptor(methodName, getInterceptor(aClass));
                    }
                }
            }
        });
        return true;
    }


    public static Interceptor getInterceptor(Class<? extends Interceptor> interceptor) {
        try {
            InterceptFactory.addAllInterceltor(interceptor, interceptor.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return InterceptFactory.getAllInterceptors().get(interceptor);
    }
}
