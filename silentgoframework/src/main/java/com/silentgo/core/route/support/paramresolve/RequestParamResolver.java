package com.silentgo.core.route.support.paramresolve;

import com.silentgo.core.aop.MethodParam;
import com.silentgo.core.route.ParameterValueResolver;
import com.silentgo.core.route.support.paramresolve.annotation.ParameterResolver;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

/**
 * Project : silentgo
 * com.silentgo.core.route.support.paramresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/22.
 */
@ParameterResolver
public class RequestParamResolver implements ParameterValueResolver<Request> {

    @Override
    public Request getValue(Response response, Request request, MethodParam methodParam) {
        return request;
    }
}
