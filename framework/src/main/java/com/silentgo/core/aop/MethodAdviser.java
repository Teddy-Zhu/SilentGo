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

    //build when AnnotationInterceptor load
    private Map<Annotation, IAnnotation> annotationMap = new HashMap<>();

    private List<Annotation> annotations = new ArrayList<>();
    //from InterceptorFactory
    private List<Interceptor> interceptors = new ArrayList<>();

    private List<AspectMethod> aspectMethods = new ArrayList<>();

    public String getName() {
        return name;
    }

    public List<MethodParam> getParams() {
        return params;
    }

    public Map<Annotation, IAnnotation> getAnnotationMap() {
        return annotationMap;
    }

    public MethodAdviser(String className, String name, FastMethod method, List<MethodParam> params, List<Annotation> annotations) {
        this.name = name;
        this.className = className;
        this.method = method;
        this.params = params;
        this.annotations = annotations;
    }

    public List<Interceptor> getInterceptors() {
        return interceptors;
    }

    public String getClassName() {
        return className;
    }

    public boolean hasAnnotation(Class<? extends Annotation> clz) {
        return annotationMap.containsKey(clz);
    }

    public boolean addInterceptor(Interceptor interceptor) {
        return CollectionKit.ListAdd(interceptors, interceptor);
    }

    public boolean addInterceptor(List<Interceptor> interceptor) {
        return CollectionKit.ListAdd(interceptors, interceptor);
    }


    public boolean addIAnnotation(Annotation annotation, IAnnotation anInterceptor) {
        return CollectionKit.MapAdd(annotationMap, annotation, anInterceptor);
    }

    public boolean addAspectMethod(AspectMethod aspectMethod) {
        return CollectionKit.ListAdd(aspectMethods, aspectMethod);
    }

    public void sortAnnotationMap() {
        List<Map.Entry<Annotation, IAnnotation>> list = new ArrayList<>(annotationMap.entrySet());
        Collections.sort(list, (o1, o2) -> {
            int x = o1.getValue().priority();
            int y = o2.getValue().priority();
            return (x < y) ? -1 : ((x == y) ? 0 : 1);
        });

    }

    public void sortInterceptrs() {
        interceptors.sort(((o1, o2) -> {
            int x = o1.priority();
            int y = o2.priority();
            return (x < y) ? -1 : ((x == y) ? 0 : 1);
        }));

    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public List<AspectMethod> getAspectMethods() {
        return aspectMethods;
    }
}
