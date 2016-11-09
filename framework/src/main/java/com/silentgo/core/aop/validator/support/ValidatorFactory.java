package com.silentgo.core.aop.validator.support;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.aop.MethodParam;
import com.silentgo.core.aop.support.MethodAOPFactory;
import com.silentgo.core.aop.validator.IValidator;
import com.silentgo.core.aop.validator.annotation.RequestString;
import com.silentgo.core.aop.validator.annotation.Validator;
import com.silentgo.core.build.Factory;
import com.silentgo.core.exception.AppBuildException;
import com.silentgo.core.exception.AppReleaseException;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.utils.ClassKit;
import com.silentgo.utils.CollectionKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.aop.validator
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by  on 16/7/18.
 */
@Factory
public class ValidatorFactory extends BaseFactory {

    private static Logger LOGGER = LoggerFactory.getLogger(ValidatorFactory.class);
    /**
     * Key : Annotation Class Name  Value : Sorted ValidatorInterceptor
     */
    private Map<Class<? extends Annotation>, IValidator> validatorHashMap = new HashMap<>();

    private Map<Method, Map<String, Map<Annotation, IValidator>>> methodParamValidatorMap = new HashMap<>();

    public ValidatorFactory() {
        validatorHashMap.put(RequestString.class, new StringValidator());
    }

    public boolean addValidator(Class<? extends Annotation> clz, IValidator validator) {
        return CollectionKit.MapAdd(validatorHashMap, clz, validator);
    }

    public IValidator getValidator(Class<? extends Annotation> an) {
        return validatorHashMap.get(an);
    }

    public boolean addMethodParamValidator(Method name, Map<String, Map<Annotation, IValidator>> validatorsMap) {
        return CollectionKit.MapAdd(methodParamValidatorMap, name, validatorsMap);
    }


    public Map<String, Map<Annotation, IValidator>> getParamValidatorMap(Method name) {
        return methodParamValidatorMap.get(name);
    }

    @Override
    public boolean initialize(SilentGo me) throws AppBuildException {

        me.getAnnotationManager().getClasses(Validator.class).forEach(aClass -> {
            if (IValidator.class.isAssignableFrom(aClass)) {
                Class<? extends Annotation> an = (Class<? extends Annotation>) ClassKit.getGenericClass(aClass, 0);
                try {
                    if (addValidator(an, (IValidator) aClass.newInstance())) {
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
        MethodAOPFactory methodAOPFactory = me.getFactory(MethodAOPFactory.class);
        methodAOPFactory.getMethodAdviserMap().forEach((k, v) ->
                addMethodParamValidator(v.getName(), buildIValidator(v)));

        return true;
    }

    @Override
    public boolean destroy(SilentGo me) throws AppReleaseException {
        return false;
    }

    private Map<String, Map<Annotation, IValidator>> buildIValidator(MethodAdviser adviser) {

        Map<String, Map<Annotation, IValidator>> methodParamsMap = new HashMap<>();

        //build IValidator
        for (MethodParam methodParam : adviser.getParams()) {
            Map<Annotation, IValidator> validatorMap = new HashMap<>();

            methodParam.getAnnotations().forEach(annotation -> {

                IValidator iValidator = getValidator(annotation.annotationType());

                CollectionKit.MapAdd(validatorMap, annotation, iValidator);

            });
            CollectionKit.MapAdd(methodParamsMap, methodParam.getName(), validatorMap);
        }

        return methodParamsMap;
    }

}
