package com.silentgo.core.exception.support;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.aop.MethodParam;
import com.silentgo.core.aop.support.MethodAOPFactory;
import com.silentgo.core.build.SilentGoBuilder;
import com.silentgo.core.build.annotation.Builder;
import com.silentgo.core.exception.AppBuildException;
import com.silentgo.core.exception.annotaion.ExceptionHandler;
import com.silentgo.core.route.annotation.Controller;
import com.silentgo.core.route.annotation.Route;
import com.silentgo.kit.CollectionKit;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

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
@Builder
public class ExceptionBuilder extends SilentGoBuilder {
    @Override
    public Integer priority() {
        return 55;
    }

    @Override
    public boolean build(SilentGo me) throws AppBuildException {
        ExceptionFactory exceptionFactory = new ExceptionFactory();
        MethodAOPFactory methodAOPFactory = me.getFactory(MethodAOPFactory.class);
        me.getConfig().addFactory(exceptionFactory);
        me.getAnnotationManager().getClasses(ExceptionHandler.class).forEach(aClass -> {
            if (IExceptionHandler.class.isAssignableFrom(aClass)) {
                try {
                    exceptionFactory.addGlobalExceptionHandler((IExceptionHandler) aClass.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                Method[] methods = aClass.getDeclaredMethods();
                String classsuffix = aClass.getName() + ".";
                for (Method method : methods) {
                    String name = classsuffix + method.getName();
                    MethodAdviser adviser = methodAOPFactory.getMethodAdviser(name);
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
                        exceptionFactory.addToExceptionHandler((Class<? extends Exception>) methodParamExcetpion.getType(), adviser);
                    }
                }
            }
        });

        //build controller exception in controller class
        me.getAnnotationManager().getClasses(Controller.class).forEach(aClass -> {
            String classsuffix = aClass.getName() + ".";
            List<String> names = new ArrayList<String>();
            Map<Class<? extends Exception>, List<MethodAdviser>> map = new HashMap<>();
            for (Method method : aClass.getDeclaredMethods()) {
                if (method.getAnnotation(Route.class) != null) {
                    String name = classsuffix + method.getName();

                    names.add(name);
                }
                ExceptionHandler handler = method.getAnnotation(ExceptionHandler.class);
                if (handler != null) {
                    String name = classsuffix + method.getName();
                    MethodAdviser adviser = methodAOPFactory.getMethodAdviser(name);
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
            names.forEach(name -> exceptionFactory.addMethodExceptionHandler(name, map));
        });
        return true;
    }

    private static final ArrayList list = new ArrayList() {{
        add(Response.class);
        add(Request.class);
        add(Exception.class);
    }};

}
