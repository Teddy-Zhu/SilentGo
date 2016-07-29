package com.silentgo.core.aop.support;

import com.silentgo.core.aop.AOPPoint;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Project : silentgo
 * com.silentgo.core.aop.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/20.
 */
public class AOPInterceptor implements MethodInterceptor {
    private Object target;

    private String classPrefix;

    public AOPInterceptor(Object target) {
        this.target = target;
        classPrefix = target.getClass().getName() + ".";
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        boolean[] isResolved = new boolean[]{false};
        AOPPoint point = new AOPPoint(target, method, objects, methodProxy,
                MethodAOPFactory.getMethodAdviser(classPrefix + method.getName()), (Response) objects[0], (Request) objects[1]);

        InterceptChain chain = new InterceptChain(point, isResolved);
        point.setChain(chain);
        return point.doChain();
    }
}
