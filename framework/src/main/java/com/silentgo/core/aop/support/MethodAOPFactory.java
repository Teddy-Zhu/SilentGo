package com.silentgo.core.aop.support;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.Interceptor;
import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.aop.annotation.Aspect;
import com.silentgo.core.aop.annotationintercept.AnnotationInceptFactory;
import com.silentgo.core.aop.annotationintercept.IAnnotation;
import com.silentgo.core.aop.aspect.support.AspectFactory;
import com.silentgo.core.aop.validator.support.ValidatorFactory;
import com.silentgo.kit.CollectionKit;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.aop.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/27.
 */
public final class MethodAOPFactory {

    private MethodAOPFactory() {
    }

    private static final Map<String, MethodAdviser> methodAdviserMap = new HashMap<>();

    public static MethodAdviser getMethodAdviser(String methodName) {
        return methodAdviserMap.get(methodName);
    }

    public static boolean addMethodAdviser(MethodAdviser methodAdviser) {
        return CollectionKit.MapAdd(methodAdviserMap, methodAdviser.getName(), methodAdviser);
    }

    public static void Build(SilentGo me) {
        Map<String, List<Interceptor>> classInterceptors = InterceptBuilder.getClassInterceptors();
        methodAdviserMap.forEach((k, v) -> {

            ArrayList intercepters = new ArrayList<Interceptor>() {{
                addAll(classInterceptors.get(v.getClassName()));
            }};
            //for filter others

            //noinspection unchecked
            v.addInterceptor(intercepters);
            v.sortInterceptrs();

            buildIAnnotation(v);
            buildIValidator(v);

        });
        buildAspect(me, methodAdviserMap);
    }

    private static void buildIAnnotation(MethodAdviser adviser) {
        //build IAnnotation
        adviser.getAnnotations().forEach(annotation -> {
            adviser.addIAnnotation(annotation, AnnotationInceptFactory.getAnnotationInterceptor(annotation.annotationType()));
        });
        adviser.sortAnnotationMap();
    }

    private static void buildIValidator(MethodAdviser adviser) {
        //build IValidator
        adviser.getParams().forEach(methodParam -> {
            methodParam.getAnnotations().forEach(annotation -> {
                methodParam.addValidator(ValidatorFactory.getValidator(annotation.annotationType()));
            });
        });

    }

    public static void buildAspect(SilentGo me, Map<String, MethodAdviser> methodAdviserMap) {
        List<String> methodNames = new ArrayList<>(methodAdviserMap.keySet());
        //build aspect
        AspectFactory.getAspectMethods().forEach(aspectMethod -> {
            if (aspectMethod.isRegex()) {
                methodNames.forEach(name -> {
                    if (name.matches(aspectMethod.getRule()))
                        methodAdviserMap.get(name).addAspectMethod(aspectMethod);
                });
            } else {
                if (methodNames.contains(aspectMethod.getRule())) {
                    methodAdviserMap.get(aspectMethod.getRule()).addAspectMethod(aspectMethod);
                }
            }
        });
    }

}
