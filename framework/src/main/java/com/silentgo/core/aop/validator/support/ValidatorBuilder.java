package com.silentgo.core.aop.validator.support;

import com.silentgo.build.Builder;
import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.aop.annotationintercept.AnnotationInterceptor;
import com.silentgo.core.aop.support.MethodAOPBuilder;
import com.silentgo.core.aop.support.MethodAOPFactory;
import com.silentgo.core.aop.validator.IValidator;
import com.silentgo.core.aop.validator.annotation.Validator;
import com.silentgo.core.aop.validator.support.ValidatorFactory;
import com.silentgo.kit.ClassKit;
import com.silentgo.kit.CollectionKit;
import com.silentgo.kit.logger.Logger;
import com.silentgo.kit.logger.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.aop.validator
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/16.
 */
public class ValidatorBuilder extends Builder {

    private static final Logger LOGGER = LoggerFactory.getLog(AnnotationInterceptor.class);

    @Override
    public boolean build(SilentGo me) {
        me.getAnnotationManager().getClasses(Validator.class).forEach(aClass -> {
            if (IValidator.class.isAssignableFrom(aClass)) {
                Class<? extends Annotation> an = (Class<? extends Annotation>) ClassKit.getGenericClass(aClass, 0);
                try {
                    if (ValidatorFactory.addValidator(an, (IValidator) aClass.newInstance())) {
                        if (me.getConfig().isDevMode()) {
                            LOGGER.debug("Register Custom Validator [{}] successfully", aClass.getName());
                        }
                    } else {
                        LOGGER.error("Register Custom Validator [{}] failed", aClass.getName());
                    }
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

        MethodAOPFactory.getMethodAdviserMap().forEach((k, v) -> ValidatorFactory.addMethodParamValidator(v.getName(), buildIValidator(v)));
        return true;
    }


    private static Map<String, Map<Annotation, IValidator>> buildIValidator(MethodAdviser adviser) {

        Map<String, Map<Annotation, IValidator>> methodParamsMap = new HashMap<>();

        //build IValidator
        adviser.getParams().forEach(methodParam -> {

            Map<Annotation, IValidator> validatorMap = new HashMap<>();

            methodParam.getAnnotations().forEach(annotation -> {

                IValidator iValidator = ValidatorFactory.getValidator(annotation.annotationType());

                CollectionKit.MapAdd(validatorMap, annotation, iValidator);

            });
            CollectionKit.MapAdd(methodParamsMap, methodParam.getName(), validatorMap);
        });
        return methodParamsMap;
    }

}
