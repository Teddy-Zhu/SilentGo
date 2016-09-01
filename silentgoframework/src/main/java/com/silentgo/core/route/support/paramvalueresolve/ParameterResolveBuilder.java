package com.silentgo.core.route.support.paramvalueresolve;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.aop.MethodParam;
import com.silentgo.core.aop.support.MethodAOPFactory;
import com.silentgo.core.build.SilentGoBuilder;
import com.silentgo.core.build.annotation.Builder;
import com.silentgo.core.exception.AppParameterPaserException;
import com.silentgo.core.route.ParameterValueResolver;
import com.silentgo.core.route.annotation.Controller;
import com.silentgo.core.route.annotation.Route;
import com.silentgo.core.route.support.paramvalueresolve.annotation.ParameterResolver;

import java.lang.reflect.Method;

/**
 * Project : silentgo
 * com.silentgo.core.route.support.paramvalueresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/22.
 */
@Builder
public class ParameterResolveBuilder extends SilentGoBuilder {
    @Override
    public Integer priority() {
        return 60;
    }

    @Override
    public boolean build(SilentGo me) {

        ParameterResolveFactory parameterResolveFactory = new ParameterResolveFactory();
        me.getConfig().addFactory(parameterResolveFactory);
        me.getAnnotationManager().getClasses(ParameterResolver.class).forEach(aClass -> {
            if (!ParameterValueResolver.class.isAssignableFrom(aClass)) return;
            try {
                parameterResolveFactory.addParameterResolver((ParameterValueResolver) aClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        parameterResolveFactory.resortParameterResolvers();

        MethodAOPFactory methodAOPFactory = me.getFactory(MethodAOPFactory.class);

        me.getAnnotationManager().getClasses(Controller.class).forEach(aClass -> {
            for (Method method : aClass.getDeclaredMethods()) {
                if (method.getAnnotation(Route.class) == null) return;

                MethodAdviser adviser = methodAOPFactory.getMethodAdviser(method);
                for (MethodParam methodParam : adviser.getParams()) {
                    try {
                        parameterResolveFactory.addMethodParameterValueResolver(methodParam);
                    } catch (AppParameterPaserException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        return true;
    }
}
