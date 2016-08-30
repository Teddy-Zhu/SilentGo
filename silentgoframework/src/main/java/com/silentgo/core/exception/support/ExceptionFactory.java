package com.silentgo.core.exception.support;

import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.kit.ClassKit;
import com.silentgo.kit.CollectionKit;
import com.silentgo.kit.PropKit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.exception.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/29.
 */
public class ExceptionFactory extends BaseFactory {

    /**
     * global exception handlers
     */
    private List<IExceptionHandler> globalExceptionHandlers = new ArrayList<>();
    /**
     * include handler in controller class or in IExceptionHandler but not global
     * the method can only use the three of the parameters
     * 1. Resonpse 2. Request 3. Exception
     */
    private Map<String, Map<Class<? extends Exception>, List<MethodAdviser>>> methodHandler = new HashMap<>();

    private Map<Class<? extends Exception>, List<MethodAdviser>> toExcetionHandler = new HashMap<>();

    public boolean addGlobalExceptionHandler(IExceptionHandler exceptionHandler) {
        return CollectionKit.ListAdd(globalExceptionHandlers, exceptionHandler);
    }


    public boolean addToExceptionHandler(Class<? extends Exception> exception, MethodAdviser adviser) {
        CollectionKit.ListMapAdd(toExcetionHandler, exception, adviser);
        return true;
    }

    public boolean addMethodExceptionHandler(String name, Map<Class<? extends Exception>, List<MethodAdviser>> map) {
        CollectionKit.MapAdd(methodHandler, name, map);
        return true;
    }

    public List<MethodAdviser> getEexceptionHandler(String name, Class<? extends Exception> eclass) {
        Map<Class<? extends Exception>, List<MethodAdviser>> map = methodHandler.get(name);
        if (map == null) {
            return filterExceptionHandler(toExcetionHandler, eclass);
        } else {
            return filterExceptionHandler(map, eclass);
        }
    }

    private List<MethodAdviser> filterExceptionHandler(Map<Class<? extends Exception>, List<MethodAdviser>> map, Class<? extends Exception> eclass) {
        for (Class<? extends Exception> aClass : map.keySet()) {
            if (aClass.isAssignableFrom(eclass)) {
                return map.get(aClass);
            }
        }
        return null;
    }

}
