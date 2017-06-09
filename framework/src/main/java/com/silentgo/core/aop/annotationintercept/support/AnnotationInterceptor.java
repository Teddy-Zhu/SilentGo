package com.silentgo.core.aop.annotationintercept.support;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.AOPPoint;
import com.silentgo.core.aop.Interceptor;
import com.silentgo.core.aop.annotation.Intercept;
import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;

/**
 * Project : silentgo
 * com.silentgo.core.aop.annotation
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/20.
 */
public class AnnotationInterceptor implements Interceptor {


    private static final Log LOGGER = LogFactory.get();
    @Override
    public int priority() {
        return 5;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object resolve(AOPPoint point) throws Throwable {
        Long start = System.currentTimeMillis();
        AnnotationInceptFactory annotationInceptFactory = SilentGo.me().getFactory(AnnotationInceptFactory.class);
        Object ret = new AnnotationInterceptChain(point, annotationInceptFactory.getSortedAnnotationMap(point.getAdviser().getName())).intercept();
        LOGGER.debug("end annotation Intercept : {}", System.currentTimeMillis() - start);
        return ret;
    }
}
