package com.silentgo.core.aop.support;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.AOPPoint;
import com.silentgo.core.aop.Interceptor;
import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.aop.annotation.Intercept;
import com.silentgo.servlet.SilentGoContext;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

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
        MethodAOPFactory methodAOPFactory = SilentGo.me().getFactory(MethodAOPFactory.class);
        MethodAdviser adviser = methodAOPFactory.getMethodAdviser(method);

        if (adviser == null) {
            return methodProxy.invokeSuper(o, objects);
        }
        List<Interceptor> intercepts = methodAOPFactory.getBuildedMethodInterceptors(adviser.getMethod());

        if (intercepts.size() == 0) {
            return methodProxy.invokeSuper(o, objects);
        }

        SilentGoContext ctx = SilentGo.me().getConfig().getCtx().get();

        AOPPoint point = new AOPPoint(o, method, objects, methodProxy,
                adviser, ctx != null ? ctx.getResponse() : null, ctx != null ? ctx.getRequest() : null);
        InterceptChain chain = new InterceptChain(point, intercepts);
        point.setChain(chain);
        return point.proceed();
    }

}
