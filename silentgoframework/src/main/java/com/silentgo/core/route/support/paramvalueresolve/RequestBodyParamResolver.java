package com.silentgo.core.route.support.paramvalueresolve;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.silentgo.core.aop.MethodParam;
import com.silentgo.core.config.Const;
import com.silentgo.core.exception.AppParameterPaserException;
import com.silentgo.core.route.ParameterValueResolver;
import com.silentgo.core.route.annotation.RequestBody;
import com.silentgo.core.route.support.paramvalueresolve.annotation.ParameterResolver;
import com.silentgo.kit.typeconvert.support.TypeConvert;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.List;

/**
 * Project : silentgo
 * com.silentgo.core.route.support.paramvalueresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/30.
 */
@ParameterResolver
public class RequestBodyParamResolver implements ParameterValueResolver {
    @Override
    public boolean isValid(Response response, Request request, MethodParam methodParam) {
        return methodParam.existAnnotation(RequestBody.class);
    }

    @Override
    public Object getValue(Response response, Request request, MethodParam methodParam) throws AppParameterPaserException {
        RequestBody requestBody = methodParam.getAnnotation(RequestBody.class);
        Object ret = null;
        try {
            boolean notHasName = Const.DEFAULT_NONE.equals(requestBody.value());
            if (TypeConvert.isBaseType(methodParam.getType())) {
                if (notHasName) {
                    ret = JSON.parseObject(request.getParameterJson().getString(methodParam.getName()), methodParam.getType());
                } else {
                    ret = JSON.parseObject(request.getParameterJson().getString(requestBody.value()), methodParam.getType());
                }
            } else {
                if (notHasName) {
                    ret = JSON.parseObject(request.getJsonString(), methodParam.getType());
                } else {
                    ret = request.getParameterJson().getObject(methodParam.getName(), methodParam.getType());
                }
            }
        } catch (Exception e) {
            ret = null;
            if (methodParam.getType().isPrimitive())
                throw new AppParameterPaserException(e.getMessage());
        }

        return ret;
    }


}
