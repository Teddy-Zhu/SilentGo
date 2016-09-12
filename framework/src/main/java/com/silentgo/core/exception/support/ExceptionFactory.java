package com.silentgo.core.exception.support;

import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.ioc.bean.BeanFactory;
import com.silentgo.core.render.renderresolver.RenderResolverFactory;
import com.silentgo.core.render.support.RenderFactory;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;
import com.silentgo.utils.CollectionKit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    private Map<Method, Map<Class<? extends Exception>, List<MethodAdviser>>> methodHandler = new HashMap<>();

    private Map<Class<? extends Exception>, List<MethodAdviser>> toExcetionHandler = new HashMap<>();

    public boolean addGlobalExceptionHandler(IExceptionHandler exceptionHandler) {
        return CollectionKit.ListAdd(globalExceptionHandlers, exceptionHandler);
    }


    public boolean addToExceptionHandler(Class<? extends Exception> exception, MethodAdviser adviser) {
        CollectionKit.ListMapAdd(toExcetionHandler, exception, adviser);
        return true;
    }

    public boolean addMethodExceptionHandler(Method name, Map<Class<? extends Exception>, List<MethodAdviser>> map) {
        CollectionKit.MapAdd(methodHandler, name, map);
        return true;
    }

    private List<MethodAdviser> getEexceptionHandler(Method name, Class<? extends Exception> eclass) {
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

    public void handle(RenderResolverFactory renderResolverFactory, RenderFactory renderFactory, BeanFactory beanFactory, MethodAdviser adviser, Request request, Response response, Exception ex) throws Exception {
        Exception e = null;
        if (ex instanceof InvocationTargetException) {
            e = (Exception) ((InvocationTargetException) ex).getTargetException();
        } else {
            e = ex;
        }
        List<MethodAdviser> advisers = getEexceptionHandler(adviser.getName(),
                e.getClass());

        if (advisers == null || advisers.size() == 0) {
            throw e;
        } else {
            for (MethodAdviser exceptionMethodAdviser : advisers) {
                Object expRet = exceptionMethodAdviser.getMethod().invoke(
                        beanFactory.getBean(exceptionMethodAdviser.getClassName()).getBean(),
                        ExceptionKit.getArgs(exceptionMethodAdviser, e, response, request));
                if (renderResolverFactory.render(renderFactory, exceptionMethodAdviser, request, response, expRet)) {
                    //allow only one render enables
                    break;
                }
            }
        }
    }
}
