package com.silentgo.core.aop;

import com.silentgo.core.aop.validator.IValidator;
import com.silentgo.core.aop.validator.ValidatorInterceptor;
import com.silentgo.logger.Logger;
import com.silentgo.logger.LoggerFactory;
import com.silentgo.servlet.http.Request;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core
 *
 * @author <Acc href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</Acc>
 *         <p>
 *         Created by teddyzhu on 16/7/27.
 */
public class MethodParam {

    public static final Logger LOGGER = LoggerFactory.getLog(MethodParam.class);

    private Class<?> type;

    private String name;

    private List<Annotation> annotations;

    private Map<Annotation, IValidator> validatorMap = new HashMap<>();

    public Class<?> getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public MethodParam(Class<?> type, String name, List<Annotation> annotations) {
        this.type = type;
        this.name = name;
        this.annotations = annotations;
    }

    public boolean addValidator(IValidator iValidator) {
        Class<?> an = ((ParameterizedType) iValidator.getClass().getGenericSuperclass()).getActualTypeArguments()[0].getClass();

        if (an != null) {
            Annotation annotation = findAnnotation(an);
            if (annotation != null && !validatorMap.containsKey(annotation))
                validatorMap.put(annotation, iValidator);
            return true;
        }
        return false;
    }

    private Annotation findAnnotation(Class<?> clz) {
        return annotations.stream().filter(annotation -> annotation.annotationType().equals(clz)).findFirst().get();
    }

    public Map<Annotation, IValidator> getValidatorMap() {
        return validatorMap;
    }

    public Object getValue(Request request) {
        return request.getParsedParameter(name);
    }


}
