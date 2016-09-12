package com.silentgo.core.route.support.paramvalueresolve;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.MethodParam;
import com.silentgo.core.exception.AppParameterPaserException;
import com.silentgo.core.route.ParameterValueResolver;
import com.silentgo.core.route.annotation.RequestBody;
import com.silentgo.core.route.annotation.RequestParam;
import com.silentgo.core.route.support.paramvalueresolve.annotation.ParameterResolver;
import com.silentgo.servlet.SilentGoContext;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

import java.lang.reflect.Array;
import java.util.Collection;

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
    public boolean isValid(MethodParam methodParam) {
        return methodParam.existAnnotation(RequestBody.class);
    }

    @Override
    public Object getValue(Response response, Request request, MethodParam methodParam) throws AppParameterPaserException {
        RequestBody requestBody = methodParam.getAnnotation(RequestBody.class);
        RequestParam requestParam = methodParam.getAnnotation(RequestParam.class);
        SilentGoContext context = SilentGo.getInstance().getConfig().getCtx().get();
        Object ret = null;
        try {
            boolean notHasName = requestParam == null;

            if (notHasName) {
                if (methodParam.getType().isArray()) {
                    context.getParameterJsonArray().toArray(
                            new Object[]{Array.newInstance(methodParam.getType().getComponentType(),
                                    context.getParameterJsonArray().size())});
                } else if (Collection.class.isAssignableFrom(methodParam.getType())) {
                    ret = context.getParameterJsonArray();
                } else {
                    ret = JSON.parseObject(context.getJsonString(), methodParam.getType());
                }
            } else {
                JSONObject jsonObject = context.getParameterJson();
                if (jsonObject != null) {
                    ret = jsonObject.getObject(methodParam.getName(), methodParam.getType());
                }
            }

        } catch (Exception e) {
            ret = null;
            if (methodParam.getType().isPrimitive())
                throw new AppParameterPaserException(e.getMessage());
        }

        return ret;
    }

    @Override
    public Integer priority() {
        return 80;
    }
}
