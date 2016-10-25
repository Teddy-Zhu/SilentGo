package com.silentgo.core.route.support.paramresolver;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.MethodParam;
import com.silentgo.core.exception.AppParameterPaserException;
import com.silentgo.core.route.ParameterValueResolver;
import com.silentgo.core.route.annotation.RequestBody;
import com.silentgo.core.route.annotation.RequestParam;
import com.silentgo.core.route.support.paramresolver.annotation.ParameterResolver;
import com.silentgo.servlet.SilentGoContext;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

import java.lang.reflect.Array;
import java.util.Collection;

/**
 * Project : silentgo
 * com.silentgo.core.route.support.paramresolver
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
        RequestParam requestParam = methodParam.getAnnotation(RequestParam.class);
        SilentGo me = SilentGo.me();
        SilentGoContext context = me.getConfig().getCtx().get();
        Object ret = null;
        try {
            if (requestParam == null) {
                if (methodParam.getType().isArray()) {
                    ret = me.json().toObject(context.getJsonString(), Array.newInstance(methodParam.getType().getComponentType(), 0).getClass());
                } else if (Collection.class.isAssignableFrom(methodParam.getType())) {
                    ret = me.json().toObjectList(context.getJsonString(), methodParam.getType());
                } else {
                    ret = me.json().toObject(context.getJsonString(), methodParam.getType());
                }
            } else {
                if (methodParam.getType().isArray()) {
                    ret = me.json().toObjectArray(methodParam.getName(), context.getJsonObject(), methodParam.getType().getComponentType());
                } else if (Collection.class.isAssignableFrom(methodParam.getType())) {
                    ret = me.json().toObjectList(methodParam.getName(), context.getJsonObject(), methodParam.getType());
                } else {
                    ret = me.json().toObject(methodParam.getName(), context.getJsonObject(), methodParam.getType());
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
