package com.silentgo.core.aop;

import com.silentgo.utils.logger.Logger;
import com.silentgo.utils.logger.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

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

    private List<Class<? extends Annotation>> anTypes;

    private List<Annotation> annotations;

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
        anTypes = new ArrayList<>();
        annotations.forEach(annotation -> anTypes.add(annotation.annotationType()));
    }

    public boolean existAnnotation(Class<? extends Annotation> clz) {
        return anTypes.contains(clz);
    }

    public <T extends Annotation> T getAnnotation(Class<T> tClass) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(tClass)) {
                return (T) annotation;
            }
        }
        return null;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }
}
