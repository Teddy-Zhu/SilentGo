package com.silentgo.core.validator;

import com.silentgo.core.validator.annotation.RequestString;
import com.silentgo.logger.Logger;
import com.silentgo.logger.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;

/**
 * Project : silentgo
 * com.silentgo.core.validator
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by  on 16/7/18.
 */
public class ValidatorFactory {

    private static Logger LOGGER = LoggerFactory.getLog(ValidatorFactory.class);

    private static HashMap<String, IValidator> validatorHashMap = new HashMap<>();

    static {
        validatorHashMap.put(RequestString.class.getName(), new StringValidator());

    }

    @SuppressWarnings("unchecked")
    public static void AddValidator(Class<? extends IValidator> clz) {
        Class<?> an = ((ParameterizedType) clz.getGenericSuperclass()).getActualTypeArguments()[0].getClass();

        if (an != null) {
            try {
                AddValidator(an.getName(), clz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                LOGGER.error("Add Validator Error,Class : [{}]", clz.getName());
                e.printStackTrace();
            }
        }

    }

    public static void AddValidator(String clz, IValidator validator) {
        validatorHashMap.put(clz, validator);
    }

}
