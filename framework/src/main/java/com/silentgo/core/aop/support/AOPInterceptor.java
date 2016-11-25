package com.silentgo.core.aop.support;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.AOPPoint;
import com.silentgo.core.aop.Interceptor;
import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.aop.annotation.Intercept;
import com.silentgo.core.route.annotation.Controller;
import com.silentgo.servlet.SilentGoContext;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(AOPInterceptor.class);

    public AOPInterceptor(Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Long start = System.currentTimeMillis();

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

        boolean isNull = ctx == null;

        AOPPoint point = new AOPPoint(o, method, objects, methodProxy,
                adviser, isNull ? null : ctx.getResponse(), isNull ? null : ctx.getRequest());

        InterceptChain chain = new InterceptChain(point, intercepts);
        point.setChain(chain);

        Object returnVal = point.proceed();

        LOGGER.debug("aop method speed :{}", System.currentTimeMillis() - start);

        return returnVal;
    }

}
