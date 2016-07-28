package com.silentgo.core.aop;

import com.silentgo.core.SilentGo;

/**
 * Project : silentgo
 * com.silentgo.core.aop
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/27.
 */
public interface Interceptor {

    default boolean build(SilentGo me) {
        return true;
    }

    default int priority() {
        return Integer.MIN_VALUE;
    }

    Object resolve(AOPPoint point, boolean[] isResolved) throws Throwable;
}
