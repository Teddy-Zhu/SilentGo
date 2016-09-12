package com.silentgo.core.route.support.paramvalueresolve;

import com.silentgo.core.aop.MethodParam;
import com.silentgo.core.exception.AppParameterPaserException;
import com.silentgo.core.route.ParameterValueResolver;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;
import com.silentgo.utils.CollectionKit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Project : silentgo
 * com.silentgo.core.route.support.paramvalueresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/22.
 */
public class ParameterResolveFactory extends BaseFactory {

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

}
