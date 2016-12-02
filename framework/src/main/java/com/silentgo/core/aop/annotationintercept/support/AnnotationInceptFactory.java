package com.silentgo.core.aop.annotationintercept.support;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.aop.annotationintercept.IAnnotation;
import com.silentgo.core.aop.annotationintercept.annotation.CustomInterceptor;
import com.silentgo.core.aop.support.MethodAOPFactory;
import com.silentgo.core.build.Factory;
import com.silentgo.core.exception.AppReleaseException;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.utils.ClassKit;
import com.silentgo.utils.CollectionKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Project : silentgo
 * com.silentgo.core.aop.annotationintercept
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/27.
 */
@Factory
public class AnnotationInceptFactory extends BaseFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationInceptFactory.class);
    /**
     * Key : Method Name  Value : Sorted AnnotationInterceptor
     */
    private Map<Class<? extends Annotation>, IAnnotation> annotationInterceptorMap = new HashMap<>();

    private Map<Method, Map<Annotation, IAnnotation>> iAnnotationMap = new HashMap<>();

    private Map<Method, List<Map.Entry<Annotation, IAnnotation>>> sortedIAnnotationMap = new HashMap<>();


    public boolean addAnnotationInterceptor(Class<? extends Annotation> clz, IAnnotation interceptor) {
        return CollectionKit.MapAdd(annotationInterceptorMap, clz, interceptor);
    }

    public IAnnotation getAnnotationInterceptor(Class<? extends Annotation> clz) {
        return annotationInterceptorMap.get(clz);
    }

    private boolean addIAnnotation(Method name, Map<Annotation, IAnnotation> anMap) {
        CollectionKit.MapAdd(iAnnotationMap, name, anMap);
        return true;
    }

    public List<Map.Entry<Annotation, IAnnotation>> getSortedAnnotationMap(Method name) {
        return sortedIAnnotationMap.getOrDefault(name, new ArrayList<>());
    }

    public Map<Annotation, IAnnotation> getAnnotationMap(Method name) {
        return iAnnotationMap.get(name);
    }

    private boolean addSortedIAnnotation(Method name, List<Map.Entry<Annotation, IAnnotation>> iAnnotationMap) {
        return CollectionKit.MapAdd(sortedIAnnotationMap, name, iAnnotationMap);
    }


    @Override
    public boolean initialize(SilentGo me) {

        me.getAnnotationManager().getClasses(CustomInterceptor.class).forEach(aClass -> buildCustomInterceptor(aClass, me));

        me.getConfig().getAnnotationIntecepters().forEach(aClass -> buildCustomInterceptor(aClass, me));

        return true;
    }

    @Override
    public boolean destroy(SilentGo me) throws AppReleaseException {
        return true;
    }


    public void buildCustomInterceptor(Class<?> aClass, SilentGo me) {
        if (IAnnotation.class.isAssignableFrom(aClass)) {
            Class<? extends Annotation> an = (Class<? extends Annotation>) ClassKit.getGenericClass(aClass, 0);
            try {
                if (addAnnotationInterceptor(an, (IAnnotation) aClass.newInstance())) {
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
    }

    public void buildIAnnotation(MethodAdviser adviser) {

        Map<Annotation, IAnnotation> annotationIAnnotationMap = new HashMap<>();
        //build IAnnotation
        adviser.getAnnotations().forEach(annotation -> CollectionKit.MapAdd(annotationIAnnotationMap, annotation, getAnnotationInterceptor(annotation.annotationType())));

        addSortedIAnnotation(adviser.getName(), sortAnnotationMap(annotationIAnnotationMap));
        addIAnnotation(adviser.getName(), annotationIAnnotationMap);

    }

    private List<Map.Entry<Annotation, IAnnotation>> sortAnnotationMap(Map<Annotation, IAnnotation> annotationMap) {
        List<Map.Entry<Annotation, IAnnotation>> list = new ArrayList<>(annotationMap.entrySet());
        Collections.sort(list, (o1, o2) -> {
            int x = o1.getValue().priority();
            int y = o2.getValue().priority();
            return (x < y) ? -1 : ((x == y) ? 0 : 1);
        });
        return list;
    }
}
