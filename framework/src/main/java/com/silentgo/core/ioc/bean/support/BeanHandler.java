package com.silentgo.core.ioc.bean.support;

import com.silentgo.core.ioc.rbean.BeanModel;

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
public interface BeanHandler {
    public <T extends Annotation> boolean preHandle(T t, Class<?> clz);

    public <T extends Annotation> void handle(T t, Class<?> clz, List<BeanModel> beanDefinitions);
}
