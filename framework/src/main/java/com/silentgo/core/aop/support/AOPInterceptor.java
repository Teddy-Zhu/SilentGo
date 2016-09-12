package com.silentgo.core.aop.support;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.AOPPoint;
import com.silentgo.servlet.SilentGoContext;
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

    public AOPInterceptor(Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        SilentGoContext ctx = SilentGo.getInstance().getConfig().getCtx().get();
        MethodAOPFactory methodAOPFactory = SilentGo.getInstance().getFactory(MethodAOPFactory.class);
        AOPPoint point = new AOPPoint(o, method, objects, methodProxy,
                methodAOPFactory.getMethodAdviser(method), ctx.getResponse(), ctx.getRequest());
        InterceptChain chain = new InterceptChain(point, methodAOPFactory.getBuildedMethodInterceptors(point.getAdviser().getMethod().getJavaMethod()));
        point.setChain(chain);
        return point.proceed();
    }

}
