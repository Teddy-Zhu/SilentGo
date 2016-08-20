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
        return Integer.MIN_VALUE;
    }

    Object resolve(AOPPoint point, boolean[] isResolved) throws Throwable;
}
