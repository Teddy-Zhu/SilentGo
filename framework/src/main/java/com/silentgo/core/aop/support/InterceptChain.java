package com.silentgo.core.aop.support;

import com.silentgo.core.aop.AOPPoint;
import com.silentgo.core.aop.Interceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * Project : silentgo
 * com.silentgo.core.aop.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/20.
 */
public class InterceptChain {
    public int index;

    private AOPPoint point;

    private List<Interceptor> interceptors;

    private int size = 0;

    public InterceptChain(AOPPoint point, List<Interceptor> interceptors) {
        this.point = point;
        this.interceptors = interceptors == null ? new ArrayList<>() : interceptors;
        this.index = 0;
        this.size = this.interceptors.size();
    }

    public Object resolve() throws Throwable {
        Object returnValue = null;
        if (index < size) {
            returnValue = interceptors.get(index++).resolve(point);

        } else if (index++ == size) {
            return point.resolve();
        }

        return returnValue;

    }

}
