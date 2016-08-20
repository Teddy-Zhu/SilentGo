package com.silentgo.core.aop;

import com.silentgo.core.aop.validator.IValidator;
import com.silentgo.kit.logger.Logger;
import com.silentgo.kit.logger.LoggerFactory;
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
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/27.
 */
public class MethodParam {

    public static final Logger LOGGER = LoggerFactory.getLog(MethodParam.class);

    private Class<?> type;

    private String name;

    private List<Annotation> annotations;

    public Class<?> getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public MethodParam(Class<?> type,  String name, List<Annotation> annotations) {
        this.type = type;
        this.name = name;
        this.annotations = annotations;
    }


    public Object getValue(Request request) {
        return request.getParsedParameter(name);
    }


    public List<Annotation> getAnnotations() {
        return annotations;
    }
}
