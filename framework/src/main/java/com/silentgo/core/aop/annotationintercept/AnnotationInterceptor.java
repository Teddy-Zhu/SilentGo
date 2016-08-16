package com.silentgo.core.aop.annotationintercept;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.AOPPoint;
import com.silentgo.core.aop.Interceptor;
import com.silentgo.core.aop.annotationintercept.annotation.CustomInterceptor;
import com.silentgo.core.aop.support.InterceptChain;
import com.silentgo.kit.ClassKit;
import com.silentgo.kit.logger.Logger;
import com.silentgo.kit.logger.LoggerFactory;

import java.lang.annotation.Annotation;

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

        return new AnnotationInterceptChain(point, isResolved, AnnotationInceptFactory.getSortedAnnotationMap(point.getAdviser().getName())).intercept();
    }
}
