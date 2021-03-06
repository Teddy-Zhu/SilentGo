package com.silentgo.core.route.support.paramresolver;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.aop.MethodParam;
import com.silentgo.core.build.Factory;
import com.silentgo.core.exception.AppBuildException;
import com.silentgo.core.exception.AppParameterPaserException;
import com.silentgo.core.exception.AppReleaseException;
import com.silentgo.core.route.ParameterValueResolver;
import com.silentgo.core.route.support.RouteFactory;
import com.silentgo.core.route.support.paramresolver.annotation.ParameterResolver;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;
import com.silentgo.utils.CollectionKit;
import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Project : silentgo
 * com.silentgo.core.route.support.paramresolver
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/22.
 */
@Factory
public class ParameterResolveFactory extends BaseFactory {


    private static final Log LOGGER = LogFactory.get();
    private ParameterValueResolver defaultResolver = new DefaultParamResolver();

    private List<ParameterValueResolver> parameterValueResolverList = new ArrayList<>();

    private HashMap<MethodParam, ParameterValueResolver> parameterValueResolverHashMap = new HashMap<>();

    public ParameterValueResolver matchParameterValueResolver(MethodParam param) throws AppParameterPaserException {
        for (ParameterValueResolver resolver : parameterValueResolverList) {
            if (resolver.isValid(param)) {
                return resolver;
            }
        }
        return defaultResolver;
    }

    public Object getParameter(Request request, Response response, MethodParam param) throws AppParameterPaserException {
        return parameterValueResolverHashMap.get(param).getValue(response, request, param);
    }

    public boolean addParameterResolver(ParameterValueResolver resolver) {
        return CollectionKit.ListAdd(parameterValueResolverList, resolver);
    }

    public void resortParameterResolvers() {
        parameterValueResolverList.sort(((o1, o2) -> {
            int x = o1.priority();
            int y = o2.priority();
            return (x < y) ? -1 : ((x == y) ? 0 : 1);
        }));
    }

    public boolean addMethodParameterValueResolver(MethodParam name) throws AppParameterPaserException {
        ParameterValueResolver parameterValueResolver = matchParameterValueResolver(name);
        CollectionKit.MapAdd(parameterValueResolverHashMap, name, parameterValueResolver);
        return true;
    }

    @Override
    public boolean initialize(SilentGo me) throws AppBuildException {

        me.getAnnotationManager().getClasses(ParameterResolver.class).forEach(aClass -> {
            if (!ParameterValueResolver.class.isAssignableFrom(aClass)) return;
            try {
                addParameterResolver((ParameterValueResolver) aClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        resortParameterResolvers();

        RouteFactory routeFactory = me.getFactory(RouteFactory.class);

        routeFactory.getHashRoute().forEach((k, listv) -> listv.forEach(basicRoute -> {
            MethodAdviser adviser = basicRoute.getAdviser();
            //LOGGER.info("find adviser", adviser.getName());
            for (MethodParam methodParam : adviser.getParams()) {
                try {
                    addMethodParameterValueResolver(methodParam);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
        routeFactory.getRegexRoute().forEach(route -> {
            MethodAdviser adviser = route.getAdviser();
            //LOGGER.info("find adviser", adviser.getName());
            for (MethodParam methodParam : adviser.getParams()) {
                try {
                    addMethodParameterValueResolver(methodParam);
                } catch (Exception e) {
                    e.printStackTrace();
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
