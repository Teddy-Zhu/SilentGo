package com.silentgo.core.aop.annotationintercept.support;

import com.silentgo.config.Const;
import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.AOPPoint;
import com.silentgo.core.aop.Interceptor;

/**
 * Project : silentgo
 * com.silentgo.core.aop.annotation
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/20.
 */
public class AnnotationInterceptor implements Interceptor {


    @SuppressWarnings("unchecked")
    @Override
    public Object resolve(AOPPoint point, boolean[] isResolved) throws Throwable {
        AnnotationInceptFactory annotationInceptFactory = (AnnotationInceptFactory) SilentGo.getInstance().getConfig().getFactory(Const.AnnotationInceptFactory);
        return new AnnotationInterceptChain(point, isResolved, annotationInceptFactory.getSortedAnnotationMap(point.getAdviser().getName())).intercept();
    }
}
