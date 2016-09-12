package com.silentgo.core.route.support.paramvalueresolve;

import com.silentgo.core.aop.MethodParam;
import com.silentgo.core.exception.AppParameterPaserException;
import com.silentgo.core.route.ParameterValueResolver;
import com.silentgo.core.route.support.paramvalueresolve.annotation.ParameterResolver;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

import java.io.IOException;
import java.io.Writer;

/**
 * Project : silentgo
 * com.silentgo.core.route.support.paramvalueresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/30.
 */
@ParameterResolver
public class WriterParamResolver implements ParameterValueResolver {
    @Override
    public boolean isValid(MethodParam methodParam) {
        return methodParam.getType().equals(Writer.class);
    }

    @Override
    public Object getValue(Response response, Request request, MethodParam methodParam) throws AppParameterPaserException {
        try {
            return response.getWriter();
        } catch (IOException e) {
            throw new AppParameterPaserException(e.getMessage());
        }
    }

    @Override
    public Integer priority() {
        return 85;
    }
}
