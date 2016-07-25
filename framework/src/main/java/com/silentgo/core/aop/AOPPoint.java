package com.silentgo.core.aop;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Project : silentgo
 * com.silentgo.core.aop
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by  on 16/7/18.
 */
public class AOPPoint {

    Object obj;
    Method method;
    Object[] objects;
    MethodProxy methodProxy;

    public AOPPoint(Object obj, Method method, Object[] objects, MethodProxy methodProxy) {
        this.obj = obj;
        this.method = method;
        this.objects = objects;
        this.methodProxy = methodProxy;
    }

    public Object getObj() {
        return obj;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getObjects() {
        return objects;
    }

    public MethodProxy getMethodProxy() {
        return methodProxy;
    }

    public Object resolve(Object[] params) throws Throwable {
        return methodProxy.invokeSuper(obj, params);
    }
}
