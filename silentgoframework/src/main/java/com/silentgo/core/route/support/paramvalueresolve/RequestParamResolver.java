package com.silentgo.core.route.support.paramvalueresolve;

import com.silentgo.core.aop.MethodParam;
import com.silentgo.core.route.ParameterValueResolver;
import com.silentgo.core.route.support.paramvalueresolve.annotation.ParameterResolver;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

/**
 * Project : silentgo
 * com.silentgo.core.route.support.paramvalueresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/22.
 */
@ParameterResolver
public class RequestParamResolver implements ParameterValueResolver {

    @Override
    public boolean isValid(Response response, Request request, MethodParam methodParam) {
        return methodParam.getType().equals(Request.class);
    }

    @Override
    public Request getValue(Response response, Request request, MethodParam methodParam) {
        return request;
    }
}
