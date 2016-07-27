package com.silentgo.core.aop;

/**
 * Project : silentgo
 * com.silentgo.core.aop
 *
 * @author <Acc href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</Acc>
 *         <p>
 *         Created by teddyzhu on 16/7/27.
 */
public interface Interceptor {

    default boolean build() {
        return true;
    }

    default int priority() {
        return Integer.MIN_VALUE;
    }

    Object resolve(AOPPoint point, boolean[] isResolved) throws Throwable;
}
