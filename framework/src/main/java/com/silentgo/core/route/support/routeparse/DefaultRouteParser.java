package com.silentgo.core.route.support.routeparse;

import com.silentgo.core.action.ActionParam;
import com.silentgo.core.route.Route;
import com.silentgo.core.route.RoutePaser;
import com.silentgo.core.route.annotation.RouteMatch;
import com.silentgo.core.route.support.RouteFactory;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.RequestMethod;
import com.silentgo.servlet.http.Response;
import com.silentgo.utils.StringKit;

import java.util.Iterator;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.route.support.routeparse
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/22.
 */
public class DefaultRouteParser implements RoutePaser {

    private static final String eq = "=";

    @Override
    public Route praseRoute(RouteFactory routeFactory, ActionParam actionParam) {
        Map<Route, Double> routeMap = routeFactory.matchRoutes(actionParam.getRequestURL());

        Iterator<Route> routes = routeMap.keySet().iterator();
        Route parsed = null;
        Double preCount = 0.0;
        for (; routes.hasNext(); ) {
            Route now = routes.next();
            Double[] matchCount = new Double[]{routeMap.get(now)};
            RouteMatch routeMatch = now.getRoute().getAdviser().getAnnotation(RouteMatch.class);
            if (routeMatch == null) {
                if (preCount < matchCount[0]) {
                    parsed = now;
                }
                preCount = matchCount[0];
                continue;
            }
            matchCount[0] += routeMatch.priority();

            Request request = actionParam.getRequest();

            if (!validateRequestMethod(routeMatch.method(), request, matchCount)) {
                routes.remove();
                continue;
            }

            if (!validateParams(routeMatch.params(), request, matchCount)) {
                routes.remove();
                continue;
            }
            if (!validatePathParams(routeMatch.pathParams(), request, matchCount)) {
                routes.remove();
                continue;
            }
            if (!validateRequestHeader(routeMatch.headers(), request, matchCount)) {
                routes.remove();
                continue;
            }
            if (!validateProduces(routeMatch.produces(), request, actionParam.getResponse(), matchCount)) {
                routes.remove();
                continue;
            }
            if (!validateConsumes(routeMatch.consumes(), request, matchCount)) {
                routes.remove();
                continue;
            }
            if (preCount < matchCount[0]) {
                parsed = now;
            }
            preCount = matchCount[0];
        }

        return parsed;
    }

    private boolean validateConsumes(String[] consumes, Request request, Double[] count) {
        String contentType = request.getContentType();
        if (StringKit.isBlank(contentType)) {
            return consumes.length == 0;
        }
        for (String consume : consumes) {
            if (contentType.contains(consume)) {
                count[0] += 1;
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean validateProduces(String[] produces, Request request, Response response, Double[] count) {
        String accept = request.getHeader("Accept");
        //default accept all;
        if (StringKit.isBlank(accept)) {
            return produces.length <= 0;
        }
        boolean acceptAll = accept.contains("*/*");
        for (String produce : produces) {
            if (acceptAll || accept.contains(produce)) {
                count[0] += 1;
            } else {
                return false;
            }
            response.setContentType(response.getContentType().endsWith(";") ?
                    (response.getContentType() + produce) : (response.getContentType() + ";" + produce));
        }
        return true;
    }

    private boolean validateParams(String[] params, Request request, Double[] count) {
        for (String param : params) {
            String[] paramh = StringKit.getLeftRight(param, eq);
            if (StringKit.isBlank(paramh[0])) return false;
            if (paramh[1].equals(request.getParameter(paramh[0]))) {
                count[0] += 1;
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean validatePathParams(String[] pathParams, Request request, Double[] count) {
        for (String pathParam : pathParams) {
            String[] paramh = StringKit.getLeftRight(pathParam, eq);
            if (StringKit.isBlank(paramh[0])) return false;
            if (paramh[1].equals(request.getPathParameter(paramh[0]))) {
                count[0] += 1;
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean validateRequestHeader(String[] headers, Request request, Double[] count) {
        for (String header : headers) {
            String[] head = StringKit.getLeftRight(header, eq);
            if (StringKit.isBlank(head[0])) return false;
            if (head[1].equals(request.getHeader(head[0]))) {
                count[0] += 1;
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean validateRequestMethod(RequestMethod[] methods, Request request, Double[] count) {
        RequestMethod method = RequestMethod.prase(request.getMethod());
        for (RequestMethod requestMethod : methods) {
            if (method.equals(requestMethod)) {
                count[0] += 1;
                return true;
            }
        }
        return false;
    }

}
