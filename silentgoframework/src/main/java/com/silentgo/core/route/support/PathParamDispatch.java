package com.silentgo.core.route.support;

import com.silentgo.core.action.ActionParam;
import com.silentgo.core.route.BasicRoute;
import com.silentgo.core.route.ParameterDispatcher;
import com.silentgo.core.route.RegexRoute;
import com.silentgo.core.route.Route;
import com.silentgo.core.route.annotation.ParamDispatcher;
import com.silentgo.core.route.support.paramresolve.ParameterResolveFactory;
import com.silentgo.kit.CollectionKit;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * Project : silentgo
 * com.silentgo.core.route.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/22.
 */
@ParamDispatcher
public class PathParamDispatch implements ParameterDispatcher {
    @Override
    public Integer priority() {
        return 1;
    }


    @Override
    public void dispatch(ParameterResolveFactory parameterResolveFactory, ActionParam param, Route route, Object[] args) {
        if (!route.isRegex()) return;

        Matcher matcher = route.getMatcher();
        String[] pathParameters = new String[matcher.groupCount()];
        for (int i = 1, len = matcher.groupCount(); i <= len; i++) {
            pathParameters[i - 1] = matcher.group(i);
        }
        Map<String, String> path = new HashMap<>();
        route.getRegexRoute().getNames().forEach(name -> CollectionKit.MapAdd(path, name, matcher.group(name)));
        param.getRequest().setPathNamedParameters(path);
        param.getRequest().setPathParameters(pathParameters);
    }
}
