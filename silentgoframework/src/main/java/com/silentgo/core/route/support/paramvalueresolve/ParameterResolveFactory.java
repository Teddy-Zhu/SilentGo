package com.silentgo.core.route.support.paramvalueresolve;

import com.silentgo.core.aop.MethodParam;
import com.silentgo.core.exception.AppParameterPaserException;
import com.silentgo.core.route.ParameterValueResolver;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.kit.ClassKit;
import com.silentgo.kit.CollectionKit;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Object getParameter(Request request, Response response, MethodParam param) throws AppParameterPaserException {
        Object ret = null;
        boolean hasResolver = false;
        for (ParameterValueResolver resolver : parameterValueResolverList) {
            if (resolver.isValid(response, request, param)) {
                hasResolver = true;
                ret = resolver.getValue(response, request, param);
                break;
            }
        }
        if (!hasResolver) {
            ret = defaultResolver.getValue(response, request, param);
        }
        return ret;
    }

    public boolean addParameterResolver(ParameterValueResolver resolver) {
        return CollectionKit.ListAdd(parameterValueResolverList, resolver);
    }

}
