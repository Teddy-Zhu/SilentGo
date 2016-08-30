package com.silentgo.core.route.support.paramvalueresolve;

import com.silentgo.core.aop.MethodParam;
import com.silentgo.core.config.Const;
import com.silentgo.core.exception.AppParameterPaserException;
import com.silentgo.core.route.ParameterValueResolver;
import com.silentgo.core.route.annotation.PathVariable;
import com.silentgo.core.route.support.paramvalueresolve.annotation.ParameterResolver;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

/**
 * Project : silentgo
 * com.silentgo.core.route.support.paramvalueresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/30.
 */
@ParameterResolver
public class PathValueParamResolver implements ParameterValueResolver {
    @Override
    public boolean isValid(Response response, Request request, MethodParam methodParam) {
        return methodParam.existAnnotation(PathVariable.class);
    }

    @Override
    public Object getValue(Response response, Request request, MethodParam methodParam) throws AppParameterPaserException {
        PathVariable pathVariable = methodParam.getAnnotation(PathVariable.class);

        return pathVariable.index() > -1 ? request.getPathParameter(pathVariable.index()) :
                Const.EmptyString.equals(pathVariable.value()) ? request.getPathParameter(methodParam.getName()) :
                        request.getPathParameter(pathVariable.value());
    }
}
