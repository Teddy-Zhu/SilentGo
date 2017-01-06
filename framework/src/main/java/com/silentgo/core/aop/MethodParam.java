package com.silentgo.core.aop;

import com.silentgo.utils.reflect.SGParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public static final Logger LOGGER = LoggerFactory.getLogger(MethodParam.class);

    SGParameter sgParameter;

    private List<Annotation> annotations;

    public Class<?> getType() {
        return sgParameter.getClz();
    }

    public String getName() {
        return sgParameter.getName();
    }

    public MethodParam(SGParameter sgParameter) {
        this.sgParameter = sgParameter;
        this.annotations = new ArrayList<>(sgParameter.getAnnotationMap().values());
    }

    public boolean existAnnotation(Class<? extends Annotation> clz) {
        return sgParameter.getAnnotationMap().containsKey(clz);
    }

    public <T extends Annotation> T getAnnotation(Class<T> tClass) {
        return (T) sgParameter.getAnnotationMap().get(tClass);
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }
}
