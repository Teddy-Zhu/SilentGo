package com.silentgo.core.route.support.paramvalueresolve.support;

import com.alibaba.fastjson.JSON;
import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.MethodParam;
import com.silentgo.core.config.Const;
import com.silentgo.servlet.http.Request;

import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.route.support.paramvalueresolve.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/30.
 */
public class ParameterResolveKit {

    public static String getJsonString(Request request, MethodParam methodParam) {

        String jsonHash = SilentGo.getInstance().getConfig().getCtx().get().getHashString();

        Object object = request.getHashMap().get(methodParam.getName());

        String jsonString =
                object == null ? jsonHash : object instanceof String ? object.toString() : JSON.toJSONString(object);

        return jsonString;
    }
}
