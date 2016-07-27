package com.silentgo.core.aop.annotationintercept;

import com.silentgo.core.aop.AOPPoint;
import com.silentgo.core.aop.Interceptor;
import com.silentgo.core.aop.validator.IValidator;
import com.silentgo.core.aop.validator.exception.ValidateException;
import com.silentgo.kit.ClassKit;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.aop.annotation
 *
 * @author <Acc href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</Acc>
 *         <p>
 *         Created by teddyzhu on 16/7/20.
 */
public interface AnnotationInterceptor extends Interceptor {


    @SuppressWarnings("unchecked")
    default Class<? extends Annotation> getAnnotationClass() {
        return (Class<? extends Annotation>) ClassKit.getGenericClass(getClass(), 0);
    }

    @Override
    default boolean build() {
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    default Object resolve(AOPPoint point, boolean[] isResolved) throws Throwable {
        Map<Annotation, IAnnotation> map = point.getAdviser().getAnnotationMap();
        Object returnValue = null;
        for (Map.Entry<Annotation, IAnnotation> entry : map.entrySet()) {
            if (isResolved[0])
                break;
            returnValue = entry.getValue().intercept(point, isResolved, entry.getKey());
        }
        return point.resolve();
    }
}
