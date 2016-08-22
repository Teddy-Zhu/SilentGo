package com.silentgo.core.aop.aspect.support;

import com.silentgo.core.aop.AOPPoint;
import com.silentgo.core.aop.aspect.AspectMethod;

import java.util.List;

/**
 * Project : silentgo
 * com.silentgo.core.aop.aspect.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/29.
 */
public class AspectChain {
    private int index;

    private AOPPoint point;

    private List<AspectMethod> aspectMethods;

    private int size = 0;

    public AspectChain(AOPPoint point, List<AspectMethod> aspectMethods) {
        this.point = point;
        this.aspectMethods = aspectMethods;
        this.size = aspectMethods == null ? 0 : aspectMethods.size();
        this.index = 0;
    }

    public Object invoke() throws Throwable {
        Object returnValue = null;
        if (index < size) {
            AspectMethod method = aspectMethods.get(index++);
            //noinspection unchecked
            returnValue = method.invoke(this, point);
        } else if (index++ == size) {
            return point.doChain();
        }

        return returnValue;

    }

    public AOPPoint getPoint() {
        return point;
    }
}
