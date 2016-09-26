package com.silentgo.core.aop.support;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.Interceptor;
import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.aop.MethodParam;
import com.silentgo.core.build.Factory;
import com.silentgo.core.config.Const;
import com.silentgo.core.exception.AppBuildException;
import com.silentgo.core.exception.AppReleaseException;
import com.silentgo.core.ioc.bean.BeanDefinition;
import com.silentgo.core.ioc.bean.BeanFactory;
import com.silentgo.core.route.annotation.PathVariable;
import com.silentgo.core.route.annotation.RequestParam;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.utils.CollectionKit;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

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
    private Map<Method, MethodAdviser> methodAdviserMap = new HashMap<>();

    private Map<Method, List<Interceptor>> buildedMethodInterceptors = new HashMap<>();

    public boolean addMethodAdviser(MethodAdviser methodAdviser) {
        return CollectionKit.MapAdd(methodAdviserMap, methodAdviser.getName(), methodAdviser);
    }

    public Map<Method, MethodAdviser> getMethodAdviserMap() {
        return methodAdviserMap;
    }

    public List<Interceptor> getBuildedMethodInterceptors(Method method) {
        return buildedMethodInterceptors.get(method);
    }

    public boolean addBuildedInterceptor(Method name, List<Interceptor> interceptors) {
        return CollectionKit.MapAdd(buildedMethodInterceptors, name, interceptors);
    }

    public MethodAdviser getMethodAdviser(Method method) {
        return methodAdviserMap.get(method);
    }

    @Override
    public boolean initialize(SilentGo me) throws AppBuildException {

        InterceptFactory interceptFactory = me.getFactory(InterceptFactory.class);


        BeanFactory beanFactory = me.getFactory(me.getConfig().getBeanClass());
        Map<String, BeanDefinition> beansMap = (Map<String, BeanDefinition>) beanFactory.getBeans();


        beansMap.forEach((k, v) -> {
            List<Annotation> annotations = Arrays.asList(v.getSourceClass().getAnnotations());
            annotations = filterAnnotation(annotations);
            Method[] methods = v.getSourceClass().getDeclaredMethods();
            FastClass clz = v.getBeanClass();
            for (Method method : methods) {
                addMethodAdviser(BuildAdviser(clz.getMethod(method), annotations));
            }
        });

        methodAdviserMap.forEach((k, v) -> {

            List interceptors = new ArrayList<Interceptor>() {{
                //add global
                addAll(me.getConfig().getInterceptors());
                //add class
                addAll(interceptFactory.getClassInterceptors().getOrDefault(v.getClassName(), new ArrayList<>()));
                //add method
                addAll(interceptFactory.getMethodInterceptors().getOrDefault(v.getName(), new ArrayList<>()));

            }};
            //for filter others


            //save method interceptors
            sortInterceptrs(interceptors);

            addBuildedInterceptor(v.getName(), interceptors);

        });
        return true;
    }

    @Override
    public boolean destroy(SilentGo me) throws AppReleaseException {
        return true;
    }

    public void sortInterceptrs(List<Interceptor> interceptors) {
        interceptors.sort(((o1, o2) -> {
            int x = o1.priority();
            int y = o2.priority();
            return (x < y) ? -1 : ((x == y) ? 0 : 1);
        }));

    }


    private MethodParam BuildParam(Parameter parameter) {

        Class<?> type = parameter.getType();


        List<Annotation> annotations = Arrays.asList(parameter.getAnnotations());

        String name = parameter.getName();
        Optional<Annotation> requestParam = annotations.stream().filter(annotation -> annotation.annotationType().equals(RequestParam.class)).findFirst();
        Optional<Annotation> pathVariable = annotations.stream().filter(annotation -> annotation.annotationType().equals(PathVariable.class)).findFirst();

        if (requestParam.isPresent()) {
            String tmpName = ((RequestParam) requestParam.get()).value();
            name = Const.DEFAULT_NONE.equals(tmpName) ? name : tmpName;
        }

        return new MethodParam(type, name, annotations);
    }

    private MethodParam[] BuildParam(Parameter[] parameter) {
        MethodParam[] methodParams = new MethodParam[parameter.length];

        for (int i = 0, len = parameter.length; i < len; i++) {
            methodParams[i] = BuildParam(parameter[i]);
        }
        return methodParams;
    }

    private MethodAdviser BuildAdviser(FastMethod method, List<Annotation> parentAnnotations) {
        String className = method.getDeclaringClass().getName();
        String name = className + "." + method.getName();

        List<Annotation> annotations = Arrays.asList(method.getJavaMethod().getAnnotations());
        return new MethodAdviser(className, name, method, BuildParam(method.getJavaMethod().getParameters()), annotations);
    }

    private List<Annotation> filterAnnotation(List<Annotation> annotations) {
        return annotations.stream().filter(annotation -> !Const.KeyAnnotations.contains(annotation.annotationType())).collect(Collectors.toList());
    }

}
