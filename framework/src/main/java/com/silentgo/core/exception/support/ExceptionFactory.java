package com.silentgo.core.exception.support;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.aop.MethodParam;
import com.silentgo.core.aop.support.MethodAOPFactory;
import com.silentgo.core.build.Factory;
import com.silentgo.core.exception.AppBuildException;
import com.silentgo.core.exception.AppReleaseException;
import com.silentgo.core.exception.annotaion.ExceptionHandler;
import com.silentgo.core.ioc.bean.BeanFactory;
import com.silentgo.core.render.renderresolver.RenderResolverFactory;
import com.silentgo.core.render.support.RenderFactory;
import com.silentgo.core.route.annotation.Controller;
import com.silentgo.core.route.annotation.Route;
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
@Factory
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
            map = new HashMap<>();
        }
        List<MethodAdviser> ret = filterExceptionHandler(toExcetionHandler, eclass);
        if (ret.size() > 0) {
            return ret;
        }
        return filterExceptionHandler(map, eclass);
    }

    private List<MethodAdviser> filterExceptionHandler(Map<Class<? extends Exception>, List<MethodAdviser>> map, Class<? extends Exception> eclass) {
        for (Class<? extends Exception> aClass : map.keySet()) {
            if (aClass.isAssignableFrom(eclass)) {
                return map.get(aClass);
            }
        }
        return new ArrayList<>();
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
                        beanFactory.getBean(exceptionMethodAdviser.getClassName()).getObject(),
                        ExceptionKit.getArgs(exceptionMethodAdviser, e, response, request));
                if (renderResolverFactory.render(renderFactory, exceptionMethodAdviser, request, response, expRet)) {
                    //allow only one render enables
                    break;
                }
            }
        }
    }

    @Override
    public boolean initialize(SilentGo me) throws AppBuildException {
        MethodAOPFactory methodAOPFactory = me.getFactory(MethodAOPFactory.class);
        me.getAnnotationManager().getClasses(ExceptionHandler.class).forEach(aClass -> {
            if (IExceptionHandler.class.isAssignableFrom(aClass)) {
                try {
                    ExceptionHandler exceptionHandler = (ExceptionHandler) aClass.getAnnotation(ExceptionHandler.class);
                    if (exceptionHandler.value().length > 0) {
                        Method method = aClass.getMethod("resolve", Response.class, Request.class, Throwable.class);
                        MethodAdviser adviser = methodAOPFactory.getMethodAdviser(method);
                        for (Class<? extends Exception> aClass1 : exceptionHandler.value()) {
                            addToExceptionHandler(aClass1, adviser);
                        }
                    } else {
                        addGlobalExceptionHandler((IExceptionHandler) aClass.newInstance());
                    }

                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            } else {
                Method[] methods = aClass.getDeclaredMethods();
                for (Method method : methods) {
                    MethodAdviser adviser = methodAOPFactory.getMethodAdviser(method);
                    if (adviser.getParams().length > 3) {
                    } else {
                        boolean bc = false;
                        MethodParam methodParamExcetpion = null;
                        for (MethodParam methodParam : adviser.getParams()) {
                            if (!list.contains(methodParam.getType())) {
                                bc = true;
                            }
                            if (Exception.class.isAssignableFrom(methodParam.getType())) {
                                methodParamExcetpion = methodParam;
                            }
                        }
                        if (bc || methodParamExcetpion == null) continue;
                        addToExceptionHandler((Class<? extends Exception>) methodParamExcetpion.getType(), adviser);
                    }
                }
            }
        });

        //build controller exception in controller class
        me.getAnnotationManager().getClasses(Controller.class).forEach(aClass -> {
            List<Method> names = new ArrayList<>();
            Map<Class<? extends Exception>, List<MethodAdviser>> map = new HashMap<>();
            for (Method method : aClass.getDeclaredMethods()) {
                if (method.getAnnotation(Route.class) != null) {
                    names.add(method);
                    continue;
                }
                ExceptionHandler handler = method.getAnnotation(ExceptionHandler.class);
                if (handler != null) {
                    MethodAdviser adviser = methodAOPFactory.getMethodAdviser(method);
                    if (adviser.getParams().length > 3) {
                    } else {
                        boolean bc = false;
                        MethodParam methodParamExcetpion = null;
                        for (MethodParam methodParam : adviser.getParams()) {
                            if (!list.contains(methodParam.getType())) {
                                bc = true;
                            }
                            if (Exception.class.isAssignableFrom(methodParam.getType())) {
                                methodParamExcetpion = methodParam;
                            }
                        }
                        if (bc || methodParamExcetpion == null) continue;
                        CollectionKit.ListMapAdd(map, (Class<? extends Exception>) methodParamExcetpion.getType(), adviser);
                    }
                }
            }
            names.forEach(name -> addMethodExceptionHandler(name, map));
        });
        return true;
    }

    @Override
    public boolean destroy(SilentGo me) throws AppReleaseException {
        return true;
    }

    private static final ArrayList list = new ArrayList() {{
        add(Response.class);
        add(Request.class);
        add(Exception.class);
    }};


}
