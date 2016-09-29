package com.silentgo.core.ioc.bean.support;

import com.silentgo.core.aop.annotation.Aspect;
import com.silentgo.core.exception.annotaion.ExceptionHandler;
import com.silentgo.core.ioc.bean.BeanDefinition;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Project : parent
 * Package : com.silentgo.core.ioc.bean.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/29.
 */
public class NoInjectBeanHandler implements BeanHandler {
    @Override
    public <T extends Annotation> boolean hasHandle(T t, Class<?> clz) {
        return Aspect.class.equals(t.annotationType()) || ExceptionHandler.class.equals(t.annotationType());
    }

    @Override
    public <T extends Annotation> void handle(T t, Class<?> clz, List<BeanDefinition> beanDefinitions) {
        BeanBuildKit.commonBuild(beanDefinitions, t, clz, false);
    }
}
