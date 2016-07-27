package com.silentgo.core.aop.validator.support;

import com.silentgo.core.aop.validator.IValidator;
import com.silentgo.core.aop.validator.ValidatorInterceptor;
import com.silentgo.core.aop.validator.annotation.RequestString;
import com.silentgo.kit.ClassKit;
import com.silentgo.kit.CollectionKit;
import com.silentgo.logger.Logger;
import com.silentgo.logger.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.aop.validator
 *
 * @author <Acc href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</Acc>
 *         <p>
 *         Created by  on 16/7/18.
 */
public class ValidatorFactory {

    private static Logger LOGGER = LoggerFactory.getLog(ValidatorFactory.class);
    /**
     * Key : Annotation Class Name  Value : Sorted ValidatorInterceptor
     */
    private static Map<String, IValidator> validatorHashMap = new HashMap<>();

    static {
        validatorHashMap.put(RequestString.class.getName(), new StringValidator());

    }

    @SuppressWarnings("unchecked")
    public static void addValidator(Class<? extends IValidator> clz) {
        Class<?> an = ClassKit.getGenericClass(clz, 0);

        if (an != null) {
            try {
                addValidator(an.getName(), clz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                LOGGER.error("Add Validator Error,Class : [{}]", clz.getName());
                e.printStackTrace();
            }
        }

    }

    public static void addValidator(String name, IValidator validator) {
        CollectionKit.MapAdd(validatorHashMap, name, validator);
    }

}
