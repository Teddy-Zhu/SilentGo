package com.silentgo.core.aop.validator.support;

import com.silentgo.core.aop.validator.IValidator;
import com.silentgo.core.aop.validator.ValidatorInterceptor;
import com.silentgo.core.aop.validator.annotation.RequestString;
import com.silentgo.kit.ClassKit;
import com.silentgo.kit.CollectionKit;
import com.silentgo.logger.Logger;
import com.silentgo.logger.LoggerFactory;

import java.lang.annotation.Annotation;
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
public class ValidatorFactory {

    private static Logger LOGGER = LoggerFactory.getLog(ValidatorFactory.class);
    /**
     * Key : Annotation Class Name  Value : Sorted ValidatorInterceptor
     */
    private static Map<Class<? extends Annotation>, IValidator> validatorHashMap = new HashMap<>();

    static {
        validatorHashMap.put(RequestString.class, new StringValidator());

    }

    @SuppressWarnings("unchecked")
    public static void addValidator(Class<? extends IValidator> clz) {
        Class<? extends Annotation> an = (Class<? extends Annotation>) ClassKit.getGenericClass(clz, 0);

        if (an != null) {
            try {
                addValidator(an, clz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                LOGGER.error("Add Validator Error,Class : [{}]", clz.getName());
                e.printStackTrace();
            }
        }

    }

    public static void addValidator(Class<? extends Annotation> clz, IValidator validator) {
        CollectionKit.MapAdd(validatorHashMap, clz, validator);
    }

}
