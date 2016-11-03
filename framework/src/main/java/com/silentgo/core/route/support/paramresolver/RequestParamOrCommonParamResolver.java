package com.silentgo.core.route.support.paramresolver;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.MethodParam;
import com.silentgo.core.exception.AppParameterPaserException;
import com.silentgo.core.route.ParameterValueResolver;
import com.silentgo.core.route.annotation.RequestParam;
import com.silentgo.core.route.support.paramresolver.annotation.ParameterResolver;
import com.silentgo.servlet.SilentGoContext;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;
import com.silentgo.utils.json.JsonPaser;

import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.route.support.paramresolver
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/31.
 */
@ParameterResolver
public class RequestParamOrCommonParamResolver implements ParameterValueResolver {
    @Override
    public Integer priority() {
        return 100;
    }

    @Override
    public boolean isValid(MethodParam methodParam) {
        return true;
    }

    @Override
    public Object getValue(Response response, Request request, MethodParam methodParam) throws AppParameterPaserException {
        Object ret = null;
        SilentGo me = SilentGo.me();
        RequestParam requestParam = methodParam.getAnnotation(RequestParam.class);

        Map<String, Object> resolvedMap = request.getResolvedMap();
        SilentGoContext context = me.getConfig().getCtx().get();
        JsonPaser parser = me.json();
        ret = parser.toObject(methodParam.getName(), context.getHashObject(), methodParam.getType());
        if (ret == null && requestParam == null) {
            try {
                ret = me.json().toObject(context.getHashString(), methodParam.getType());
            } catch (Exception e) {
                ret = null;
            }
        }
        if (methodParam.getType().isPrimitive() && ret == null) {
            throw new AppParameterPaserException("Type %s parameter '%s' is not present   \n" +
                    "but cannot be translated into a null value due to being declared as a primitive type.", methodParam.getType().getName(), methodParam.getName());
        }
        return ret;
    }
}
