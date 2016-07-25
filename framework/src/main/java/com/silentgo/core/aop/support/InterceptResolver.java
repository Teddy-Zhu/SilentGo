package com.silentgo.core.aop.support;

import com.silentgo.core.aop.AOPPoint;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Project : silentgo
 * com.silentgo.core.aop.support
 *
 * @author    <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by teddyzhu on 16/7/20.
 */
public class InterceptResolver {
    public int index;

    private AOPPoint point;

    private boolean[] isHandled;

    public InterceptResolver(AOPPoint point, boolean[] isHandled) {
        this.index = 0;
        this.point = point;
        this.isHandled = isHandled;

    }

}
