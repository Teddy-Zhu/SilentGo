package com.silentgo.core.route.support;

import com.alibaba.fastjson.JSON;
import com.silentgo.core.action.ActionParam;
import com.silentgo.core.aop.MethodParam;
import com.silentgo.core.config.Const;
import com.silentgo.core.route.ParameterDispatcher;
import com.silentgo.core.route.ParameterValueResolver;
import com.silentgo.core.route.Route;
import com.silentgo.core.route.annotation.ParamDispatcher;
import com.silentgo.core.route.annotation.PathVariable;
import com.silentgo.core.route.support.paramresolve.ParameterResolveFactory;
import com.silentgo.kit.CollectionKit;
import com.silentgo.kit.StringKit;
import com.silentgo.kit.logger.Logger;
import com.silentgo.kit.logger.LoggerFactory;
import com.silentgo.kit.typeconvert.ConvertKit;
import com.silentgo.kit.typeconvert.ITypeConvertor;
import com.silentgo.kit.typeconvert.support.TypeConvert;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.route.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/22.
 */
@ParamDispatcher
public class CommonParamDispatch implements ParameterDispatcher {

    public static final Logger LOGGER = LoggerFactory.getLog(CommonParamDispatch.class);


    @Override
    public Integer priority() {
        return 5;
    }


    @Override
    public void dispatch(ParameterResolveFactory parameterResolveFactory, ActionParam param, Route route, Object[] args) {

        MethodParam[] methodParams = route.getRoute().getAdviser().getParams();
        Map<String, Object> parsedParameters = new HashMap<>();


        for (int i = 0, len = methodParams.length; i < len; i++) {
            MethodParam methodParam = methodParams[i];

            ParameterValueResolver resolver = parameterResolveFactory.getParameterValueResolver(methodParam.getType());
            try {
                args[i] = resolver.getValue(param.getResponse(), param.getRequest(), methodParam);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
                args[i] = null;
            }
            CollectionKit.MapAdd(parsedParameters, methodParam.getName(), args[i], true);

        }
    }
}
