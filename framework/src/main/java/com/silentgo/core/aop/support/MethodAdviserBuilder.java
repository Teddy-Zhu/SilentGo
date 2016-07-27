package com.silentgo.core.aop.support;

import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.aop.MethodParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

/**
 * Project : silentgo
 * com.silentgo.core.aop.support
 *
 * @author <Acc href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</Acc>
 *         <p>
 *         Created by teddyzhu on 16/7/27.
 */
public class MethodAdviserBuilder {

    public static MethodParam BuildParam(Parameter parameter) {

        Class<?> type = parameter.getType();


        List<Annotation> annotations = Arrays.asList(parameter.getAnnotations());


        return null;
    }

    public static MethodAdviser BuildAdviser(Method method) {

        String name = method.getDeclaringClass().getName() + "." + method.getName();

        return null;

    }
}
