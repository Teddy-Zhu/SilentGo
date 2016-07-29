package com.silentgo.core.aop.aspect.support;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.AOPPoint;
import com.silentgo.core.aop.Interceptor;
import com.silentgo.core.aop.annotation.Around;
import com.silentgo.core.aop.annotation.Aspect;
import com.silentgo.core.aop.aspect.AspectMethod;
import com.silentgo.core.aop.support.InterceptChain;
import com.silentgo.core.ioc.bean.BeanDefinition;
import com.silentgo.core.ioc.bean.BeanWrapper;
import net.sf.cglib.reflect.FastClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Project : silentgo
 * com.silentgo.core.aop.aspect.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/29.
 */
public class AspectInterceptor implements Interceptor {


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
        return true;
    }

    @Override
    public Object resolve(AOPPoint point, boolean[] isResolved) throws Throwable {
        return new AspectChain(point, isResolved, point.getAdviser().getAspectMethods());
    }
}
