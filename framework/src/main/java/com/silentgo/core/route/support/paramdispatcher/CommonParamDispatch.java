package com.silentgo.core.route.support.paramdispatcher;

import com.silentgo.core.action.ActionParam;
import com.silentgo.core.aop.MethodParam;
import com.silentgo.core.exception.AppParameterPaserException;
import com.silentgo.core.route.ParameterDispatcher;
import com.silentgo.core.route.Route;
import com.silentgo.core.route.support.paramdispatcher.annotation.ParamDispatcher;
import com.silentgo.core.route.support.paramresolver.ParameterResolveFactory;
import com.silentgo.utils.CollectionKit;
import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;

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

    private static final Log LOGGER = LogFactory.get();

    @Override
    public Integer priority() {
        return 100;
    }


    @Override
    public void dispatch(ParameterResolveFactory parameterResolveFactory, ActionParam param, Route route, Object[] args) throws AppParameterPaserException {

        MethodParam[] methodParams = route.getRoute().getAdviser().getParams();
        Map<String, Object> parsedParameters = new HashMap<>();


        for (int i = 0, len = methodParams.length; i < len; i++) {
            MethodParam methodParam = methodParams[i];

            args[i] = parameterResolveFactory.getParameter(param.getRequest(), param.getResponse(), methodParam);

            CollectionKit.MapAdd(parsedParameters, methodParam.getName(), args[i], true);

        }
    }
}
