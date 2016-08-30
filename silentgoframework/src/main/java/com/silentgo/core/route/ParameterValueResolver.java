package com.silentgo.core.route;

import com.silentgo.core.aop.MethodParam;
import com.silentgo.core.exception.AppParameterPaserException;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

/**
 * Project : silentgo
 * com.silentgo.core.route
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/22.
 */
public interface ParameterValueResolver {

    public boolean isValid(Response response, Request request, MethodParam methodParam);

    public Object getValue(Response response, Request request, MethodParam methodParam) throws AppParameterPaserException;
}
