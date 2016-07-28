package com.silentgo.core.aop.annotationintercept;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.AOPPoint;
import com.silentgo.core.aop.Interceptor;
import com.silentgo.core.aop.annotationintercept.annotation.CustomInterceptor;
import com.silentgo.core.aop.support.MethodAOPFactory;
import com.silentgo.core.aop.support.MethodAdviserBuilder;
import com.silentgo.kit.ClassKit;
import com.silentgo.logger.Logger;
import com.silentgo.logger.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.aop.annotation
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/20.
 */
public class AnnotationInterceptor implements Interceptor {


    public static final Logger LOGGER = LoggerFactory.getLog(AnnotationInterceptor.class);

    @SuppressWarnings("unchecked")
    @Override
    public boolean build(SilentGo me) {
        me.getAnnotationManager().getClasses(CustomInterceptor.class).forEach(aClass -> {
            if (IAnnotation.class.isAssignableFrom(aClass)) {
                Class<? extends Annotation> an = (Class<? extends Annotation>) ClassKit.getGenericClass(aClass, 0);
                try {
                    if (AnnotationInceptFactory.addAnnotationInterceptor(an, (IAnnotation) aClass.newInstance())) {
                        if (me.getConfig().isDevMode()) {
                            LOGGER.debug("Register Custom Interceptor [{}] successfully", aClass.getName());
                        }
                    } else {
                        LOGGER.error("Register Custom Interceptor [{}] failed", aClass.getName());
                    }
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        MethodAOPFactory.refreshSortIAnnotation();
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object resolve(AOPPoint point, boolean[] isResolved) throws Throwable {
        return new AnnotationInterceptChain(point, isResolved, point.getAdviser().getAnnotationMap()).excute();
    }
}
