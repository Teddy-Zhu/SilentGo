package com.silentgo.core.aop.support;

import com.silentgo.build.Builder;
import com.silentgo.config.Const;
import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.aop.MethodParam;
import com.silentgo.core.ioc.bean.BeanDefinition;
import com.silentgo.core.route.annotation.RequestParam;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * Project : silentgo
 * com.silentgo.core.aop.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/27.
 */
public class MethodAdviserBuilder extends Builder {

    private MethodAdviserBuilder() {
    }

    private static MethodParam BuildParam(Parameter parameter) {

        Class<?> type = parameter.getType();


        List<Annotation> annotations = Arrays.asList(parameter.getAnnotations());

        String name = parameter.getName();
        Optional<Annotation> requestParam = annotations.stream().filter(annotation -> annotation.annotationType().equals(RequestParam.class)).findFirst();
        if (requestParam.isPresent()) {
            String tmpName = ((RequestParam) requestParam.get()).value();
            name = Const.DEFAULT_NONE.equals(tmpName) ? name : tmpName;
        }
        return new MethodParam(type, name, annotations);
    }

    private static List<MethodParam> BuildParam(Parameter[] parameter) {
        List<MethodParam> methodParams = new ArrayList<>();
        for (Parameter parm : parameter) {
            methodParams.add(BuildParam(parm));
        }
        return methodParams;
    }

    private static MethodAdviser BuildAdviser(FastMethod method, List<Annotation> parentAnnotations) {

        String name = method.getDeclaringClass().getName() + "." + method.getName();

        List<Annotation> annotations = Arrays.asList(method.getJavaMethod().getAnnotations());

        annotations.addAll(parentAnnotations);

        return new MethodAdviser(name, method.getJavaMethod().getDeclaringClass().getName(), method, BuildParam(method.getJavaMethod().getParameters()), annotations);
    }

    private static void filterAnnotation(List<Annotation> annotations) {
        for (int i = 0, len = annotations.size(); i < len; i++) {
            if (Const.KeyAnnotations.contains(annotations.get(i).annotationType())) {
                annotations.remove(i);
                i--;
            }
        }
    }

    @Override
    public boolean build(SilentGo me) {
        Map<String, BeanDefinition> beansMap = (Map<String, BeanDefinition>) me.getConfig().getBeanFactory().getBeans();
        beansMap.forEach((k, v) -> {
            List<Annotation> annotations = Arrays.asList(v.getSourceClass().getAnnotations());
            filterAnnotation(annotations);
            Method[] methods = v.getSourceClass().getMethods();
            FastClass clz = v.getBeanClass();
            for (Method method : methods) {
                MethodAOPFactory.addMethodAdviser(BuildAdviser(clz.getMethod(method), annotations));
            }
        });
        return true;
    }
}
