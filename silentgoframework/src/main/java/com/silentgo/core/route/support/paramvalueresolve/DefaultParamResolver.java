package com.silentgo.core.route.support.paramvalueresolve;

import com.alibaba.fastjson.JSON;
import com.silentgo.core.aop.MethodParam;
import com.silentgo.core.exception.AppParameterPaserException;
import com.silentgo.core.route.ParameterValueResolver;
import com.silentgo.core.route.support.paramvalueresolve.support.ParameterResolveKit;
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
public class DefaultParamResolver implements ParameterValueResolver {
    @Override
    public boolean isValid(MethodParam methodParam) {
        return true;
    }

    @Override
    public Object getValue(Response response, Request request, MethodParam methodParam) throws AppParameterPaserException {

        if (methodParam.getType().isPrimitive()) {
            throw new AppParameterPaserException("Type %s parameter '%s' is not present   \n" +
                    "but cannot be translated into a null value due to being declared as a primitive type.", methodParam.getType().getName(), methodParam.getName());
        }
        return null;
    }

    @Override
    public Integer priority() {
        return 101;
    }
}
