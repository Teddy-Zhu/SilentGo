package com.silentgo.core.aop.annotationintercept.support;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.AOPPoint;
import com.silentgo.core.aop.Interceptor;
import com.silentgo.core.aop.annotation.Intercept;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Project : silentgo
 * com.silentgo.core.aop.annotation
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/20.
 */
@Intercept
public class AnnotationInterceptor implements Interceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationInterceptor.class);

    @Override
    public int priority() {
        return 5;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object resolve(AOPPoint point) throws Throwable {
        LOGGER.debug("start Annotaion Intercept");
        AnnotationInceptFactory annotationInceptFactory = SilentGo.me().getFactory(AnnotationInceptFactory.class);
        Object ret = new AnnotationInterceptChain(point, annotationInceptFactory.getSortedAnnotationMap(point.getAdviser().getName())).intercept();
        LOGGER.debug("end Annotaion Intercept");
        return ret;
    }
}
