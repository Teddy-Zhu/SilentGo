package com.silentgo.core.route.support.paramvalueresolve;

import com.alibaba.fastjson.JSON;
import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.MethodParam;
import com.silentgo.core.config.Const;
import com.silentgo.core.exception.AppParameterPaserException;
import com.silentgo.core.route.ParameterValueResolver;
import com.silentgo.core.route.annotation.PathVariable;
import com.silentgo.core.route.support.paramvalueresolve.annotation.ParameterResolver;
import com.silentgo.core.route.support.paramvalueresolve.support.ParameterResolveKit;
import com.silentgo.kit.typeconvert.ConvertKit;
import com.silentgo.kit.typeconvert.ITypeConvertor;
import com.silentgo.kit.typeconvert.support.TypeConvert;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.route.support.paramvalueresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/22.
 */
@ParameterResolver
public class CommomParamResolver implements ParameterValueResolver {


    @Override
    public boolean isValid(Response response, Request request, MethodParam methodParam) {
        return TypeConvert.isBaseType(methodParam.getType());
    }

    @Override
    public Object getValue(Response response, Request request, MethodParam methodParam) throws AppParameterPaserException {

        String jsonString = ParameterResolveKit.getJsonString(request, methodParam);

        ITypeConvertor typeConvertor = new ConvertKit().getTypeConvert(String.class, methodParam.getType());

        Object ret = typeConvertor.convert(jsonString);
        if (methodParam.getType().isPrimitive() && ret == null) {
            throw new AppParameterPaserException("Type %s parameter '%s' is not present   \n" +
                    "but cannot be translated into a null value due to being declared as a primitive type.", methodParam.getType().getName(), methodParam.getName());
        }
        return ret;

    }


}
