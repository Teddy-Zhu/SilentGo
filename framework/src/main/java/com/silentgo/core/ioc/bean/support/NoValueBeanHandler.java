package com.silentgo.core.ioc.bean.support;

import com.silentgo.core.aop.annotationintercept.annotation.CustomInterceptor;
import com.silentgo.core.exception.annotaion.ExceptionHandler;
import com.silentgo.core.ioc.rbean.BeanModel;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Project : SilentGo
 * Package : com.silentgo.core.ioc.bean.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/13.
 */
public class NoValueBeanHandler implements BeanHandler {

    private static final ArrayList anList = new ArrayList() {{
        add(CustomInterceptor.class);
        add(ExceptionHandler.class);
    }};


    @Override
    public <T extends Annotation> boolean preHandle(T t, Class<?> clz) {
        return anList.contains(t.annotationType()) && !clz.isInterface();
    }

    @Override
    public <T extends Annotation> void handle(T t, Class<?> clz, List<BeanModel> beanDefinitions) {
        BeanBuildKit.commonBuildNoValue(beanDefinitions, clz, true);
    }
}
