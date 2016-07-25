package com.silentgo.core.aop;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Project : silentgo
 * com.silentgo.core.aop.annotation
 *
 * @author    <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by teddyzhu on 16/7/20.
 */
public interface IInterceptor<T extends Annotation>  {
    default int priority() {
        return Integer.MIN_VALUE;
    }

    Object resolve(AOPPoint point,T t) throws Throwable;

}
