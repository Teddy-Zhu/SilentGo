package com.silentgo.core.render.renderresolver;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.aop.support.MethodAOPFactory;
import com.silentgo.core.build.Factory;
import com.silentgo.core.exception.AppBuildException;
import com.silentgo.core.exception.AppReleaseException;
import com.silentgo.core.exception.AppRenderException;
import com.silentgo.core.exception.annotaion.ExceptionHandler;
import com.silentgo.core.render.RenderModel;
import com.silentgo.core.render.renderresolver.annotation.RenderResolve;
import com.silentgo.core.render.support.RenderFactory;
import com.silentgo.core.route.annotation.Controller;
import com.silentgo.core.route.annotation.Route;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;
import com.silentgo.utils.CollectionKit;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Project : silentgo
 * com.silentgo.core.render.renderresolver
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/30.
 */
@Factory
public class RenderResolverFactory extends BaseFactory {

    private List<RenderResolver> renderResolvers = new ArrayList<>();

    private HashMap<Method, RenderResolver> renderResolverHashMap = new HashMap<>();


    public void addRenderResolver(MethodAdviser adviser) {
        for (RenderResolver renderResolver : renderResolvers) {
            if (renderResolver.match(adviser)) {
                CollectionKit.MapAdd(renderResolverHashMap, adviser.getName(), renderResolver);
                break;
            }
        }
    }

    public boolean addRenderResolver(RenderResolver renderResolver) {
        return CollectionKit.ListAdd(renderResolvers, renderResolver);
    }

    public RenderResolver getRenderResolver(Method name) {
        return renderResolverHashMap.get(name);
    }


    public void resortRenderResolver() {
        renderResolvers.sort(((o1, o2) -> {
            int x = o1.priority();
            int y = o2.priority();
            return (x < y) ? -1 : ((x == y) ? 0 : 1);
        }));
    }

    public boolean render(RenderFactory renderFactory, MethodAdviser adviser, Request request, Response response, Object returnVal) throws AppRenderException {
        RenderResolver renderResolver = getRenderResolver(adviser.getName());

        if (renderResolver != null) {
            RenderModel renderModel = renderResolver.getRenderModel(renderFactory, adviser, response, request, returnVal);

            if (renderModel != null) {
                renderModel.render();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean initialize(SilentGo me) throws AppBuildException {

        MethodAOPFactory methodAOPFactory = me.getFactory(MethodAOPFactory.class);
        me.getAnnotationManager().getClasses(RenderResolve.class).forEach(aClass -> {
            if (RenderResolver.class.isAssignableFrom(aClass)) {
                try {
                    addRenderResolver((RenderResolver) aClass.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        resortRenderResolver();
        me.getAnnotationManager().getClasses(Controller.class).forEach(aClass -> {

            for (Method method : aClass.getDeclaredMethods()) {
                if (method.getAnnotation(Route.class) != null || method.getAnnotation(ExceptionHandler.class) != null) {
                    addRenderResolver(methodAOPFactory.getMethodAdviser(method));
                }
            }
        });
        return true;
    }

    @Override
    public boolean destroy(SilentGo me) throws AppReleaseException {
        return false;
    }
}
