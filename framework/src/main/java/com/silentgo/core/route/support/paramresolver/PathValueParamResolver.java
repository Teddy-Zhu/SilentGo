package com.silentgo.core.route.support.paramresolver;

import com.silentgo.core.aop.MethodParam;
import com.silentgo.core.config.Const;
import com.silentgo.core.exception.AppParameterPaserException;
import com.silentgo.core.route.ParameterValueResolver;
import com.silentgo.core.route.annotation.PathVariable;
import com.silentgo.core.route.support.paramresolver.annotation.ParameterResolver;
import com.silentgo.core.typeconvert.ConvertKit;
import com.silentgo.core.typeconvert.ITypeConvertor;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;
import com.silentgo.utils.TypeConvertKit;

/**
 * Project : silentgo
 * com.silentgo.core.route.support.paramresolver
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/30.
 */
@ParameterResolver
public class PathValueParamResolver implements ParameterValueResolver {
    @Override
    public boolean isValid(MethodParam methodParam) {
        return methodParam.existAnnotation(PathVariable.class);
    }

    @Override
    public Object getValue(Response response, Request request, MethodParam methodParam) throws AppParameterPaserException {
        if (!TypeConvertKit.isBaseType(methodParam.getType())) return null;
        PathVariable pathVariable = methodParam.getAnnotation(PathVariable.class);
        String jsonString = pathVariable.index() > -1 ? request.getPathParameter(pathVariable.index()) :
                Const.EmptyString.equals(pathVariable.value()) ? request.getPathParameter(methodParam.getName()) :
                        request.getPathParameter(pathVariable.value());

        ITypeConvertor typeConvertor = new ConvertKit().getTypeConvert(String.class, methodParam.getType());

        Object ret = typeConvertor.convert(jsonString);
        if (methodParam.getType().isPrimitive() && ret == null) {
            throw new AppParameterPaserException("Type %s parameter '%s' is not present   \n" +
                    "but cannot be translated into a null value due to being declared as a primitive type.", methodParam.getType().getName(), methodParam.getName());
        }
        return ret;
    }

    @Override
    public Integer priority() {
        return 5;
    }
}
