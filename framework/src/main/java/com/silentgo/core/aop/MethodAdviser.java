package com.silentgo.core.aop;

import com.silentgo.core.aop.annotationintercept.IAnnotation;
import com.silentgo.core.aop.aspect.AspectMethod;
import com.silentgo.kit.CollectionKit;
import net.sf.cglib.reflect.FastMethod;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Project : silentgo
 * com.silentgo.core.aop
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/27.
 */
public class MethodAdviser {

    private String name;

    private String className;

    private FastMethod method;

    private List<MethodParam> params;

    private List<Annotation> annotations = new ArrayList<>();

    public String getName() {
        return name;
    }

    public List<MethodParam> getParams() {
        return params;
    }
    public MethodAdviser(String className, String name, FastMethod method, List<MethodParam> params, List<Annotation> annotations) {
        this.name = name;
        this.className = className;
        this.method = method;
        this.params = params;
        this.annotations = annotations;
    }

    public String getClassName() {
        return className;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

}
