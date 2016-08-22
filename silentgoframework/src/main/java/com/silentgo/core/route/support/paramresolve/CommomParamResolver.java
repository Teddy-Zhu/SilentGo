package com.silentgo.core.route.support.paramresolve;

import com.alibaba.fastjson.JSON;
import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.MethodParam;
import com.silentgo.core.config.Const;
import com.silentgo.core.route.ParameterValueResolver;
import com.silentgo.core.route.annotation.PathVariable;
import com.silentgo.kit.typeconvert.ConvertKit;
import com.silentgo.kit.typeconvert.ITypeConvertor;
import com.silentgo.kit.typeconvert.support.TypeConvert;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.route.support.paramresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/22.
 */
public class CommomParamResolver implements ParameterValueResolver<Object> {
    @Override
    public Object getValue(Response response, Request request, MethodParam methodParam) {

        Map<String, Object> hash = request.getHashMap();

        String jsonHash = SilentGo.getInstance().getConfig().getCtx().get().getHashString();
        if (jsonHash == null) {
            jsonHash = JSON.toJSONString(hash);
            jsonHash = jsonHash.equals("{}") ? Const.EmptyString : jsonHash;
            SilentGo.getInstance().getConfig().getCtx().get().setHashString(jsonHash);
        }
        Object object = hash.get(methodParam.getName());
        String jsonString = methodParam.getAnnotations().stream().anyMatch(annotation ->
                PathVariable.class.equals(annotation.annotationType())) ?
                request.getPathParameter(methodParam.getName()) :
                (object == null ? jsonHash :
                        (object instanceof String ?
                                object.toString() : JSON.toJSONString(object)));

        if (methodParam.getType().isArray()) {
            return JSON.parseArray(jsonString, methodParam.getType());
        } else if (TypeConvert.isBaseType(methodParam.getType())) {
            ITypeConvertor typeConvertor = new ConvertKit().getTypeConvert(String.class, methodParam.getType());
            return typeConvertor.convert(jsonString);
        }

        return JSON.parseObject(jsonString, methodParam.getType());
    }


}
