package com.silentgo.core.aop.validator.support;

import com.silentgo.build.SilentGoBuilder;
import com.silentgo.build.annotation.Builder;
import com.silentgo.config.Const;
import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.aop.MethodParam;
import com.silentgo.core.aop.annotationintercept.support.AnnotationInterceptor;
import com.silentgo.core.aop.support.MethodAOPFactory;
import com.silentgo.core.aop.validator.IValidator;
import com.silentgo.core.aop.validator.annotation.Validator;
import com.silentgo.kit.ClassKit;
import com.silentgo.kit.CollectionKit;
import com.silentgo.kit.logger.Logger;
import com.silentgo.kit.logger.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.aop.validator
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/16.
 */
@Builder
public class ValidatorBuilder extends SilentGoBuilder {

    private static final Logger LOGGER = LoggerFactory.getLog(AnnotationInterceptor.class);

    @Override
    public Integer priority() {
        return 45;
    }

    @Override
    public boolean build(SilentGo me) {
        ValidatorFactory validatorFactory = new ValidatorFactory();

        me.getConfig().addFactory(validatorFactory);
        me.getAnnotationManager().getClasses(Validator.class).forEach(aClass -> {
            if (IValidator.class.isAssignableFrom(aClass)) {
                Class<? extends Annotation> an = (Class<? extends Annotation>) ClassKit.getGenericClass(aClass, 0);
                try {
                    if (validatorFactory.addValidator(an, (IValidator) aClass.newInstance())) {
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
        MethodAOPFactory methodAOPFactory = (MethodAOPFactory) me.getConfig().getFactory(Const.MethodAOPFactory);
        methodAOPFactory.getMethodAdviserMap().forEach((k, v) ->
                validatorFactory.addMethodParamValidator(v.getName(), buildIValidator(v, validatorFactory)));

        return true;
    }


    private static Map<String, Map<Annotation, IValidator>> buildIValidator(MethodAdviser adviser, ValidatorFactory validatorFactory) {

        Map<String, Map<Annotation, IValidator>> methodParamsMap = new HashMap<>();

        //build IValidator
        for (MethodParam methodParam : adviser.getParams()) {
            Map<Annotation, IValidator> validatorMap = new HashMap<>();

            methodParam.getAnnotations().forEach(annotation -> {

                IValidator iValidator = validatorFactory.getValidator(annotation.annotationType());

                CollectionKit.MapAdd(validatorMap, annotation, iValidator);

            });
            CollectionKit.MapAdd(methodParamsMap, methodParam.getName(), validatorMap);
        }

        return methodParamsMap;
    }

}
