package com.silentgo.core.render.renderresolver;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.support.MethodAOPFactory;
import com.silentgo.core.build.SilentGoBuilder;
import com.silentgo.core.build.annotation.Builder;
import com.silentgo.core.exception.AppBuildException;
import com.silentgo.core.exception.annotaion.ExceptionHandler;
import com.silentgo.core.render.renderresolver.annotation.RenderResolve;
import com.silentgo.core.route.annotation.Controller;
import com.silentgo.core.route.annotation.Route;

import java.lang.reflect.Method;

/**
 * Project : silentgo
 * com.silentgo.core.render.renderresolver
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/30.
 */
@Builder
public class RenderResolverBuilder extends SilentGoBuilder {

    @Override
    public Integer priority() {
        return 55;
    }

    @Override
    public boolean build(SilentGo me) throws AppBuildException {

        RenderResolverFactory renderResolverFactory = new RenderResolverFactory();
        me.getConfig().addFactory(renderResolverFactory);

        MethodAOPFactory methodAOPFactory = me.getFactory(MethodAOPFactory.class);
        me.getAnnotationManager().getClasses(RenderResolve.class).forEach(aClass -> {
            if (RenderResolver.class.isAssignableFrom(aClass)) {
                try {
                    renderResolverFactory.addRenderResolver((RenderResolver) aClass.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        renderResolverFactory.resortRenderResolver();
        me.getAnnotationManager().getClasses(Controller.class).forEach(aClass -> {

            for (Method method : aClass.getDeclaredMethods()) {
                if (method.getAnnotation(Route.class) != null || method.getAnnotation(ExceptionHandler.class) != null) {
                    renderResolverFactory.addRenderResolver(methodAOPFactory.getMethodAdviser(method));
                }
            }
        });
        return true;
    }
}
