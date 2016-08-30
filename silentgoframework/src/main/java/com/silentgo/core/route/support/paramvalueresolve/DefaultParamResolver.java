package com.silentgo.core.route.support.paramvalueresolve;

import com.alibaba.fastjson.JSON;
import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.MethodParam;
import com.silentgo.core.config.Const;
import com.silentgo.core.exception.AppParameterPaserException;
import com.silentgo.core.route.ParameterValueResolver;
import com.silentgo.core.route.support.paramvalueresolve.support.ParameterResolveKit;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.route.support.paramvalueresolve
 *
 * @author    <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by teddyzhu on 16/8/30.
 */
public class DefaultParamResolver implements ParameterValueResolver {
    @Override
    public boolean isValid(Response response, Request request, MethodParam methodParam) {
        return true;
    }

    @Override
    public Object getValue(Response response, Request request, MethodParam methodParam) throws AppParameterPaserException {

        String jsonString = ParameterResolveKit.getJsonString(request, methodParam);

        return JSON.parseObject(jsonString, methodParam.getType());
    }
}
