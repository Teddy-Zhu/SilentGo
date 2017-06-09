package com.silentgo.core.aop.annotationintercept.support;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.aop.annotationintercept.IAnnotation;
import com.silentgo.core.aop.annotationintercept.annotation.CustomInterceptor;
import com.silentgo.core.build.Factory;
import com.silentgo.core.exception.AppReleaseException;
import com.silentgo.core.ioc.bean.BeanFactory;
import com.silentgo.core.ioc.bean.BeanWrapper;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.utils.ClassKit;
import com.silentgo.utils.CollectionKit;
import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;

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

    private static final Log LOGGER = LogFactory.get();
    /**
     * Key : Method Name  Value : Sorted AnnotationInterceptor
     */
    private Map<Class<? extends Annotation>, BeanWrapper> annotationInterceptorMap = new HashMap<>();

    private Map<Method, Map<Annotation, BeanWrapper>> iAnnotationMap = new HashMap<>();

    private Map<Method, List<Map.Entry<Annotation, BeanWrapper>>> sortedIAnnotationMap = new HashMap<>();


    public boolean addAnnotationInterceptor(Class<? extends Annotation> clz, BeanWrapper interceptor) {
        return CollectionKit.MapAdd(annotationInterceptorMap, clz, interceptor);
    }

    public BeanWrapper getAnnotationInterceptor(Class<? extends Annotation> clz) {
        return annotationInterceptorMap.get(clz);
    }

    private boolean addIAnnotation(Method name, Map<Annotation, BeanWrapper> anMap) {
        CollectionKit.MapAdd(iAnnotationMap, name, anMap);
        return true;
    }

    public List<Map.Entry<Annotation, BeanWrapper>> getSortedAnnotationMap(Method name) {
        return sortedIAnnotationMap.getOrDefault(name, new ArrayList<>());
    }

    public Map<Annotation, BeanWrapper> getAnnotationMap(Method name) {
        return iAnnotationMap.get(name);
    }

    private boolean addSortedIAnnotation(Method name, List<Map.Entry<Annotation, BeanWrapper>> iAnnotationMap) {
        return CollectionKit.MapAdd(sortedIAnnotationMap, name, iAnnotationMap);
    }


    @Override
    public boolean initialize(SilentGo me) {

        BeanFactory beanFactory = me.getFactory(me.getConfig().getBeanClass());

        me.getAnnotationManager().getClasses(CustomInterceptor.class).forEach(aClass -> {

            buildCustomInterceptor(aClass, me, beanFactory);
        });

        me.getConfig().getAnnotationIntecepters().forEach(aClass -> buildCustomInterceptor(aClass, me, beanFactory));

        return true;
    }

    @Override
    public boolean destroy(SilentGo me) throws AppReleaseException {
        return true;
    }


    public void buildCustomInterceptor(Class<?> aClass, SilentGo me, BeanFactory beanFactory) {
        if (IAnnotation.class.isAssignableFrom(aClass)) {
            Class<? extends Annotation> an = (Class<? extends Annotation>) ClassKit.getGenericClass(aClass, 0);
            try {
                BeanWrapper bean = beanFactory.addBean(aClass.newInstance(), true, false, false);
                if (addAnnotationInterceptor(an, bean)) {
                    if (me.getConfig().isDevMode()) {
                        LOGGER.debug("Register Custom Interceptor [{}] successfully", aClass.getName());
                    }
                } else {
                    LOGGER.error("Register Custom Interceptor [{}] failed", aClass.getName());
                }
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                LOGGER.error(e);
            }
        }
    }

    public void buildIAnnotation(MethodAdviser adviser) {

        Map<Annotation, BeanWrapper> annotationIAnnotationMap = new HashMap<>();
        //build IAnnotation
        adviser.getAnnotations().forEach(annotation -> CollectionKit.MapAdd(annotationIAnnotationMap, annotation, getAnnotationInterceptor(annotation.annotationType())));

        addSortedIAnnotation(adviser.getName(), sortAnnotationMap(annotationIAnnotationMap));
        addIAnnotation(adviser.getName(), annotationIAnnotationMap);

    }

    private List<Map.Entry<Annotation, BeanWrapper>> sortAnnotationMap(Map<Annotation, BeanWrapper> annotationMap) {
        List<Map.Entry<Annotation, BeanWrapper>> list = new ArrayList<>(annotationMap.entrySet());
        Collections.sort(list, (o1, o2) -> {
            int x = ((IAnnotation) o1.getValue().getOriginObject()).priority();
            int y = ((IAnnotation) o2.getValue().getOriginObject()).priority();
            return (x < y) ? -1 : ((x == y) ? 0 : 1);
        });
        return list;
    }
}
