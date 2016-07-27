package com.silentgo.core.aop.support;

import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.kit.CollectionKit;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.aop.support
 *
 * @author <Acc href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</Acc>
 *         <p>
 *         Created by teddyzhu on 16/7/27.
 */
public class MethodAOPFactory {

    private static Map<String, MethodAdviser> methodAdviserMap = new HashMap<>();

    public static MethodAdviser getMethodAdviser(String methodName) {
        return methodAdviserMap.get(methodName);
    }

    public static boolean addMethodAdviser(MethodAdviser methodAdviser) {
        return CollectionKit.MapAdd(methodAdviserMap, methodAdviser.getName(), methodAdviser);
    }

    public static boolean addMethodAdviser(Method method) {

        return false;
    }


}
