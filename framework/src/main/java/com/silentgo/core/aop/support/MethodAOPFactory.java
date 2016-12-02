package com.silentgo.core.aop.support;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.Interceptor;
import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.aop.MethodParam;
import com.silentgo.core.aop.annotation.Aspect;
import com.silentgo.core.aop.annotationintercept.annotation.CustomInterceptor;
import com.silentgo.core.aop.annotationintercept.support.AnnotationInceptFactory;
import com.silentgo.core.aop.aspect.support.AspectFactory;
import com.silentgo.core.aop.validator.annotation.Validator;
import com.silentgo.core.aop.validator.support.ValidatorFactory;
import com.silentgo.core.build.Factory;
import com.silentgo.core.config.Const;
import com.silentgo.core.exception.AppBuildException;
import com.silentgo.core.exception.AppReleaseException;
import com.silentgo.core.exception.annotaion.ExceptionHandler;
import com.silentgo.core.ioc.annotation.Component;
import com.silentgo.core.ioc.annotation.Service;
import com.silentgo.core.ioc.bean.BeanFactory;
import com.silentgo.core.ioc.bean.BeanWrapper;
import com.silentgo.core.ioc.bean.support.BeanBuildKit;
import com.silentgo.core.route.annotation.Controller;
import com.silentgo.core.route.annotation.RequestParam;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.utils.CollectionKit;
import com.silentgo.utils.asm.LocalVariableTableParameterNameDiscoverer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodAOPFactory.class);

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
        ArrayList globalInterceptors = me.getConfig().getInterceptors();
        AnnotationInceptFactory annotationInceptFactory = me.getFactory(AnnotationInceptFactory.class);
        InterceptFactory interceptFactory = me.getFactory(InterceptFactory.class);
        AspectFactory aspectFactory = me.getFactory(AspectFactory.class);
        ValidatorFactory validatorFactory = me.getFactory(ValidatorFactory.class);

        buildMethodAdviser(globalInterceptors, interceptFactory, annotationInceptFactory, aspectFactory, validatorFactory, clz);
    }


    public void buildMethodAdviser(ArrayList globalInterceptors, InterceptFactory interceptFactory,
                                   AnnotationInceptFactory annotationInceptFactory, AspectFactory aspectFactory,
                                   ValidatorFactory validatorFactory, Class<?> clz) {
        List<Annotation> annotations = Arrays.asList(clz.getAnnotations());
        annotations = filterAnnotation(annotations);
        Method[] methods = clz.getDeclaredMethods();
        boolean needInject = false;
        for (Method method : methods) {
            MethodAdviser methodAdviser = BuildAdviser(method, annotations);

            List interceptors = new ArrayList<Interceptor>() {{
                //add global
                addAll(globalInterceptors);
                //add class
                addAll(interceptFactory.getClassInterceptors().getOrDefault(clz, new ArrayList<>()));
                //add method
                addAll(interceptFactory.getMethodInterceptors().getOrDefault(method, new ArrayList<>()));

            }};
            //for filter others

            if (interceptors.size() > 3) needInject = true;

            //save method interceptors
            sortInterceptrs(interceptors);

            addBuildedInterceptor(method, interceptors);

            addMethodAdviser(methodAdviser);

            //build annotations
            annotationInceptFactory.buildIAnnotation(methodAdviser);
            if (annotationInceptFactory.getSortedAnnotationMap(methodAdviser.getMethod()).size() > 0)
                needInject = true;

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

            if (aspectFactory.getAspectMethod(methodAdviser.getMethod()).size() > 0)
                needInject = true;

            validatorFactory.addMethodParamValidator(methodAdviser.getMethod(), validatorFactory.buildIValidator(methodAdviser));

            if (!validatorFactory.getParamValidatorMap(methodAdviser.getMethod()).isEmpty()) {
                needInject = true;
            }
        }
        initclz.put(clz, needInject);
    }

    public void sortInterceptrs(List<Interceptor> interceptors) {
        interceptors.sort(((o1, o2) -> {
            int x = o1.priority();
            int y = o2.priority();
            return (x < y) ? -1 : ((x == y) ? 0 : 1);
        }));

    }

    private MethodParam BuildParam(Parameter parameter, String name) {

        Class<?> type = parameter.getType();
        List<Annotation> annotations = Arrays.asList(parameter.getAnnotations());

        Optional<Annotation> requestParam = annotations.stream().filter(annotation -> annotation.annotationType().equals(RequestParam.class)).findFirst();

        if (requestParam.isPresent()) {
            String tmpName = ((RequestParam) requestParam.get()).value();
            name = Const.DEFAULT_NONE.equals(tmpName) ? name : tmpName;
        }

        return new MethodParam(type, name, annotations);
    }

    private MethodParam BuildParam(Parameter parameter) {
        return BuildParam(parameter, parameter.getName());
    }

    private MethodParam[] BuildParam(Method method, Parameter[] parameter) {
        MethodParam[] methodParams = new MethodParam[parameter.length];
        if (parameter.length == 0) return methodParams;
        if (parameter[0].isNamePresent()) {
            BuildParam(methodParams, parameter);
        } else {
            //copy from spring, had no good ideas
            LocalVariableTableParameterNameDiscoverer lvtd = new LocalVariableTableParameterNameDiscoverer();
            String[] parameterNames = lvtd.getParameterNames(method);
            if (parameterNames == null || parameterNames.length != parameter.length) {
                BuildParam(methodParams, parameter);
                return methodParams;
            }
            for (int i = 0, len = parameter.length; i < len; i++) {
                methodParams[i] = BuildParam(parameter[i], parameterNames[i]);
            }

        }

        return methodParams;
    }

    private void BuildParam(MethodParam[] params, Parameter[] parameters) {
        for (int i = 0, len = parameters.length; i < len; i++) {
            params[i] = BuildParam(parameters[i]);
        }
    }

    private MethodAdviser BuildAdviser(Method method, List<Annotation> parentAnnotations) {
        String className = method.getDeclaringClass().getName();
        String name = className + "." + method.getName();

        List<Annotation> annotations = Arrays.asList(method.getAnnotations());
        return new MethodAdviser(className, name, method, BuildParam(method, method.getParameters()), annotations);
    }

    private List<Annotation> filterAnnotation(List<Annotation> annotations) {
        return annotations.stream().filter(annotation -> !Const.KeyAnnotations.contains(annotation.annotationType())).collect(Collectors.toList());
    }

}
