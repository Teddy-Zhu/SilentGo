package com.silentgo.core.aop;

import com.silentgo.core.aop.support.InterceptChain;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;
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

    MethodAdviser adviser;
    Response response;
    Request request;

    InterceptChain chain;

    public AOPPoint(Object obj, Method method, Object[] objects, MethodProxy methodProxy, MethodAdviser adviser, Response response, Request request) {
        this.obj = obj;
        this.method = method;
        this.objects = objects;
        this.methodProxy = methodProxy;
        this.adviser = adviser;
        this.response = response;
        this.request = request;
    }

    public MethodAdviser getAdviser() {
        return adviser;
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

    public Object resolve() throws Throwable {
        return methodProxy.invokeSuper(obj, objects);
    }

    public Response getResponse() {
        return response;
    }

    public Request getRequest() {
        return request;
    }

    public void setChain(InterceptChain chain) {
        this.chain = chain;
    }

    public Object doChain() throws Throwable {
        return chain.resolve();
    }
}
