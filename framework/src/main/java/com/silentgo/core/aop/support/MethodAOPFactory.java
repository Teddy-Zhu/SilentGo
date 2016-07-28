package com.silentgo.core.aop.support;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.Interceptor;
import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.kit.CollectionKit;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Project : silentgo
 * com.silentgo.core.aop.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/27.
 */
public final class MethodAOPFactory {

    private MethodAOPFactory() {
    }

    private static final Map<String, MethodAdviser> methodAdviserMap = new HashMap<>();

    public static MethodAdviser getMethodAdviser(String methodName) {
        return methodAdviserMap.get(methodName);
    }

    public static boolean addMethodAdviser(MethodAdviser methodAdviser) {
        return CollectionKit.MapAdd(methodAdviserMap, methodAdviser.getName(), methodAdviser);
    }

    public static void Build(SilentGo me) {
        Map<String, List<Interceptor>> classInterceptors = InterceptBuilder.getClassInterceptors();
        methodAdviserMap.forEach((k, v) -> {
            v.addInterceptor(classInterceptors.get(v.getClassName()));
        });
    }

    public static void  refreshSortIAnnotation(){
        methodAdviserMap.forEach((k,v)-> v.sortAnnotationMap());
    }
}
