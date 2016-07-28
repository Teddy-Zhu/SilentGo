package com.silentgo.core.aop.intercept;

import com.silentgo.core.aop.Interceptor;
import com.silentgo.kit.CollectionKit;

import java.util.ArrayList;
import java.util.List;

/**
 * Project : silentgo
 * com.silentgo.core.aop.intercept
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/28.
 */
public class InterceptFactory {

    private static final List<Interceptor> globalInterceptors = new ArrayList<>();

    public static void addInterceptor(Interceptor interceptor) {
        CollectionKit.ListAdd(globalInterceptors, interceptor, false);
    }
}
