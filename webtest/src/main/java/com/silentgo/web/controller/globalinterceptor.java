package com.silentgo.web.controller;

import com.silentgo.core.aop.AOPPoint;
import com.silentgo.core.aop.Interceptor;
import com.silentgo.core.aop.annotation.*;

/**
 * Project : silentgo
 * com.silentgo.web.controller
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/31.
 */
@com.silentgo.core.aop.annotation.Intercept
public class globalinterceptor implements Interceptor {

    @Override
    public Object resolve(AOPPoint point) throws Throwable {

        Object ret = point.proceed();
        point.getRequest().setAttribute("method", point.getAdviser().getName().toString());
        return ret;
    }
}
