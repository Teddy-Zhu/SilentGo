package com.silentgo.core.aop;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.support.InterceptChain;

/**
 * Project : silentgo
 * com.silentgo.core.aop
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/27.
 */
public interface Interceptor {

    default int priority() {
        return 1000;
    }

    Object resolve(AOPPoint point) throws Throwable;
}
