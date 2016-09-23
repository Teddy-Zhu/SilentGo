package com.silentgo.core.aop.support;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.Interceptor;
import com.silentgo.core.aop.annotation.Intercept;
import com.silentgo.core.build.SilentGoBuilder;
import com.silentgo.core.build.annotation.Builder;
import com.silentgo.core.ioc.bean.BeanDefinition;
import com.silentgo.core.ioc.bean.BeanFactory;

import java.lang.reflect.Method;
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
@Builder
public class InterceptBuilder extends SilentGoBuilder {

    @Override
    public Integer priority() {
        return 15;
    }

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
        InterceptFactory interceptFactory = new InterceptFactory();
        me.getConfig().addFactory(interceptFactory);

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
        interceptFactory.addAllInterceltor(globalInterceptors);
        BeanFactory beanFactory = SilentGo.getInstance().getFactory(BeanFactory.class);


        Map<String, BeanDefinition> beanDefinitionMap = (Map<String, BeanDefinition>) beanFactory.getBeans();
        beanDefinitionMap.forEach((k, v) -> {
            Intercept classIntercept = v.getSourceClass().getAnnotation(Intercept.class);
            if (classIntercept != null && classIntercept.value().length != 0) {
                for (Class<? extends Interceptor> aClass : classIntercept.value()) {
                    interceptFactory.addClassInterceptor(v.getSourceClass().getName(), getInterceptor(interceptFactory, aClass));
                }
            }
            Method[] methods = v.getSourceClass().getDeclaredMethods();
            for (Method method : methods) {
                Intercept intercept = method.getAnnotation(Intercept.class);
                if (intercept != null && intercept.value().length != 0) {
                    for (Class<? extends Interceptor> aClass : intercept.value()) {
                        interceptFactory.addMethodInterceptor(method, getInterceptor(interceptFactory, aClass));
                    }
                }
            }
        });

        return true;
    }


    public static Interceptor getInterceptor(InterceptFactory interceptFactory, Class<? extends Interceptor> interceptor) {
        try {
            interceptFactory.addAllInterceltor(interceptor, interceptor.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return interceptFactory.getAllInterceptors().get(interceptor);
    }
}
