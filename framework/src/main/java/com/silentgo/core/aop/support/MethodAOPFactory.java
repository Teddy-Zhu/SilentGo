package com.silentgo.core.aop.support;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.Interceptor;
import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.aop.annotation.Aspect;
import com.silentgo.core.aop.annotationintercept.annotation.CustomInterceptor;
import com.silentgo.core.aop.annotationintercept.support.AnnotationInceptFactory;
import com.silentgo.core.aop.annotationintercept.support.AnnotationInterceptor;
import com.silentgo.core.aop.aspect.support.AspectFactory;
import com.silentgo.core.aop.aspect.support.AspectInterceptor;
import com.silentgo.core.aop.validator.annotation.Validator;
import com.silentgo.core.aop.validator.support.ValidatorFactory;
import com.silentgo.core.aop.validator.support.ValidatorInterceptor;
import com.silentgo.core.build.Factory;
import com.silentgo.core.config.Const;
import com.silentgo.core.exception.AppBuildException;
import com.silentgo.core.exception.AppReleaseException;
import com.silentgo.core.exception.annotaion.ExceptionHandler;
import com.silentgo.core.ioc.annotation.Component;
import com.silentgo.core.ioc.annotation.Service;
import com.silentgo.core.route.annotation.Controller;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.utils.CollectionKit;
import com.silentgo.utils.ReflectKit;
import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;
import com.silentgo.utils.reflect.SGClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Project : silentgo
 * com.silentgo.core.aop.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/16.
 */
@Factory
public class MethodAOPFactory extends BaseFactory {


    private static final Log LOGGER = LogFactory.get();

    private Map<Method, MethodAdviser> methodAdviserMap = new HashMap<>();

    private Map<Class<?>, Boolean> initclz = new HashMap<>();

    private Map<Method, List<Interceptor>> buildedMethodInterceptors = new HashMap<>();

    private boolean addMethodAdviser(MethodAdviser methodAdviser) {
        return CollectionKit.MapAdd(methodAdviserMap, methodAdviser.getName(), methodAdviser);
    }

    public Map<Method, MethodAdviser> getMethodAdviserMap() {
        return methodAdviserMap;
    }

    public Map<Class<?>, Boolean> getInitclz() {
        return initclz;
    }

    public List<Interceptor> getBuildedMethodInterceptors(Method method) {
        return buildedMethodInterceptors.getOrDefault(method, new ArrayList<>());
    }

    public boolean addBuildedInterceptor(Method name, List<Interceptor> interceptors) {
        return CollectionKit.MapAdd(buildedMethodInterceptors, name, interceptors);
    }

    public MethodAdviser getMethodAdviser(Method method) {
        MethodAdviser adviser = methodAdviserMap.get(method);
        if (adviser == null && !method.getDeclaringClass().isInterface()) {
            buildMethodAdviser(method.getDeclaringClass());
            adviser = methodAdviserMap.get(method);
        }
        return adviser;
    }

    private static ArrayList<Class<? extends Annotation>> anList = new ArrayList() {{
        add(Service.class);
        add(Component.class);
        add(Controller.class);
        add(Aspect.class);
        add(ExceptionHandler.class);
        add(CustomInterceptor.class);
        add(Validator.class);
    }};

    @Override
    public boolean initialize(SilentGo me) throws AppBuildException {

        InterceptFactory interceptFactory = me.getFactory(InterceptFactory.class);
        AnnotationInceptFactory annotationInceptFactory = me.getFactory(AnnotationInceptFactory.class);
        AspectFactory aspectFactory = me.getFactory(AspectFactory.class);
        ValidatorFactory validatorFactory = me.getFactory(ValidatorFactory.class);
        return true;
    }

    @Override
    public boolean destroy(SilentGo me) throws AppReleaseException {
        return true;
    }

    public boolean hasInterceptors(Class<?> clz) {
        return initclz.getOrDefault(clz, false);
    }

    public boolean hasInitClass(Class<?> clz) {
        return initclz.containsKey(clz);
    }

    public void buildMethodAdviser(Class<?> clz) {
        if (initclz.containsKey(clz)) return;
        if (clz.isInterface()) return;

        SilentGo me = SilentGo.me();
        List<Interceptor> globalInterceptors = me.getConfig().getInterceptors();
        AnnotationInceptFactory annotationInceptFactory = me.getFactory(AnnotationInceptFactory.class);
        InterceptFactory interceptFactory = me.getFactory(InterceptFactory.class);
        AspectFactory aspectFactory = me.getFactory(AspectFactory.class);
        ValidatorFactory validatorFactory = me.getFactory(ValidatorFactory.class);

        buildMethodAdviser(globalInterceptors, interceptFactory, annotationInceptFactory, aspectFactory, validatorFactory, clz);
    }


    public void buildMethodAdviser(List<Interceptor> globalInterceptors, InterceptFactory interceptFactory,
                                   AnnotationInceptFactory annotationInceptFactory, AspectFactory aspectFactory,
                                   ValidatorFactory validatorFactory, Class<?> clz) {

        final boolean[] needInject = {false};
        SGClass sgClass = ReflectKit.getSGClass(clz);
        sgClass.getMethodMap().forEach((methodEntity, method) -> {
            MethodAdviser methodAdviser = new MethodAdviser(method);

            List interceptors = new ArrayList<Interceptor>() {{
                //add global
                addAll(globalInterceptors);
                //add class
                addAll(interceptFactory.getClassInterceptors().getOrDefault(clz, new ArrayList<>()));
                //add method
                addAll(interceptFactory.getMethodInterceptors().getOrDefault(method, new ArrayList<>()));

            }};
            addMethodAdviser(methodAdviser);

            if (globalInterceptors.size() > 0)
                needInject[0] = true;

            //build annotations
            annotationInceptFactory.buildIAnnotation(methodAdviser);
            if (annotationInceptFactory.getSortedAnnotationMap(methodAdviser.getMethod()).size() > 0) {
                interceptors.add(interceptFactory.getInterceptor(AnnotationInterceptor.class));
                needInject[0] = true;
            }

            aspectFactory.getAspectMethods().forEach(aspectMethod -> {
                if (aspectMethod.getMethod().getParameterCount() != 1) {
                    LOGGER.warn("The aspect Method [{}] ignored .", aspectMethod.getMethod().getName());
                    return;
                }

                if (aspectMethod.isRegex()) {
                    if (methodAdviser.getMethodName().matches(aspectMethod.getRule()))
                        aspectFactory.addAspectMethodInMap(methodAdviser.getMethod(), aspectMethod);
                } else {
                    if (methodAdviser.getMethodName().equals(aspectMethod.getRule()))
                        aspectFactory.addAspectMethodInMap(methodAdviser.getMethod(), aspectMethod);
                }
            });

            if (aspectFactory.getAspectMethod(methodAdviser.getMethod()).size() > 0) {
                interceptors.add(interceptFactory.getInterceptor(AspectInterceptor.class));
                needInject[0] = true;
            }

            validatorFactory.addMethodParamValidator(methodAdviser.getMethod(), validatorFactory.buildIValidator(methodAdviser));

            if (!validatorFactory.getParamValidatorMap(methodAdviser.getMethod()).isEmpty()) {
                interceptors.add(interceptFactory.getInterceptor(ValidatorInterceptor.class));
                needInject[0] = true;
            }

            //save method interceptors
            sortInterceptrs(interceptors);

            addBuildedInterceptor(methodAdviser.getMethod(), interceptors);


        });
        initclz.put(clz, needInject[0]);
    }

    public void sortInterceptrs(List<Interceptor> interceptors) {
        interceptors.sort(((o1, o2) -> {
            int x = o1.priority();
            int y = o2.priority();
            return (x < y) ? -1 : ((x == y) ? 0 : 1);
        }));

    }

    private List<Annotation> filterAnnotation(List<Annotation> annotations) {
        return annotations.stream().filter(annotation -> !Const.KeyAnnotations.contains(annotation.annotationType())).collect(Collectors.toList());
    }

}
