package com.silentgo.core.aop.support;

import com.silentgo.core.aop.AOPPoint;
import com.silentgo.core.aop.Interceptor;
import com.silentgo.core.aop.annotationintercept.AnnotationInterceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
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

    private boolean[] isResolved;

    private List<Interceptor> interceptors;

    private int size = 0;

    public InterceptChain(AOPPoint point, boolean[] isResolved, List<Interceptor> interceptors) {
        this.point = point;
        this.isResolved = isResolved;
        this.interceptors = interceptors;
        this.index = 0;
        this.size = this.interceptors.size();
    }

    public void Init() {
        this.index = 0;
    }

    public Object excute() throws Throwable {
        Object returnValue = null;
        if (index < size) {

            Interceptor interceptor = interceptors.get(index++);

            returnValue = interceptor.resolve(point, isResolved);

            if (isResolved[0]) {
                return returnValue;
            }
        } else if (index++ == size) {
            return point.resolve();
        }

        return returnValue;

    }

}
