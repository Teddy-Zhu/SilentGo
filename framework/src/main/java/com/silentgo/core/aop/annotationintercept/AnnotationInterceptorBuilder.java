package com.silentgo.core.aop.annotationintercept;

import com.silentgo.build.Builder;
import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.aop.annotationintercept.annotation.CustomInterceptor;
import com.silentgo.core.aop.support.MethodAOPBuilder;
import com.silentgo.core.aop.support.MethodAOPFactory;
import com.silentgo.kit.ClassKit;
import com.silentgo.kit.CollectionKit;
import com.silentgo.kit.logger.Logger;
import com.silentgo.kit.logger.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Project : silentgo
 * com.silentgo.core.aop.annotationintercept.annotation
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/16.
 */
public class AnnotationInterceptorBuilder extends Builder {


    private static final Logger LOGGER = LoggerFactory.getLog(AnnotationInterceptorBuilder.class);

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

        MethodAOPFactory.getMethodAdviserMap().forEach((k, v) -> {
            //special interceptors
            buildIAnnotation(v);
        });
        return true;
    }


    private static void buildIAnnotation(MethodAdviser adviser) {

        Map<Annotation, IAnnotation> annotationIAnnotationMap = new HashMap<>();
        //build IAnnotation
        adviser.getAnnotations().forEach(annotation -> {
            CollectionKit.MapAdd(annotationIAnnotationMap, annotation, AnnotationInceptFactory.getAnnotationInterceptor(annotation.annotationType()));
        });

        AnnotationInceptFactory.addSortedIAnnotation(adviser.getName(), sortAnnotationMap(annotationIAnnotationMap));
        AnnotationInceptFactory.addIAnnotation(adviser.getName(), annotationIAnnotationMap);

    }

    private static List<Map.Entry<Annotation, IAnnotation>> sortAnnotationMap(Map<Annotation, IAnnotation> annotationMap) {
        List<Map.Entry<Annotation, IAnnotation>> list = new ArrayList<>(annotationMap.entrySet());
        Collections.sort(list, (o1, o2) -> {
            int x = o1.getValue().priority();
            int y = o2.getValue().priority();
            return (x < y) ? -1 : ((x == y) ? 0 : 1);
        });
        return list;
    }

}
