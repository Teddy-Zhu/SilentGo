package com.silentgo.utils.reflect;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * Project : SilentGo
 * Package : com.silentgo.utils.reflect
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2017/1/5.
 */
public class AnnotationMap extends SGEntity {

    private Map<Class<? extends Annotation>, Annotation> annotationMap;


    public Map<Class<? extends Annotation>, Annotation> getAnnotationMap() {
        return annotationMap;
    }

    public void setAnnotationMap(Map<Class<? extends Annotation>, Annotation> annotationMap) {
        this.annotationMap = annotationMap;
    }

    public Annotation getAnnotation(Class<? extends Annotation> clz) {
        return annotationMap.get(clz);
    }
}
