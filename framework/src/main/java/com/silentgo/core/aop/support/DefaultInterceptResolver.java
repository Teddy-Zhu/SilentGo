package com.silentgo.core.aop.support;

import com.silentgo.core.aop.AOPPoint;
import com.silentgo.core.aop.IInterceptor;
import com.silentgo.core.aop.annotation.CustomInterceptor;
import com.silentgo.core.aop.annotation.Intercept;

/**
 * Project : silentgo
 * com.silentgo.core.aop.support
 *
 * @author    <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by teddyzhu on 16/7/20.
 */
@CustomInterceptor
public class DefaultInterceptResolver implements IInterceptor<Intercept> {
    @Override
    public int priority() {
        return 0;
    }

    @Override
    public Object resolve(AOPPoint point, Intercept intercept) throws Throwable {
        return null;
    }

}
