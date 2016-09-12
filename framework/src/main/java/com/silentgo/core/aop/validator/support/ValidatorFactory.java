package com.silentgo.core.aop.validator.support;

import com.silentgo.core.aop.validator.IValidator;
import com.silentgo.core.aop.validator.annotation.RequestString;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.utils.CollectionKit;
import com.silentgo.utils.logger.Logger;
import com.silentgo.utils.logger.LoggerFactory;

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
public class ValidatorFactory extends BaseFactory {

    private static Logger LOGGER = LoggerFactory.getLog(ValidatorFactory.class);
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

}
