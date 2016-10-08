package com.silentgo.core.route.support.routeparse;

import com.silentgo.core.action.ActionParam;
import com.silentgo.core.route.Route;
import com.silentgo.core.route.RoutePaser;
import com.silentgo.core.route.support.RouteFactory;

/**
 * Project : silentgo
 * com.silentgo.core.route.support.routeparse
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/22.
 */
public class DefaultRouteParser implements RoutePaser {
    @Override
    public Route praseRoute(RouteFactory routeFactory, ActionParam actionParam) {
        //List<Route> routes = routeFactory.matchRoutes(actionParam.getRequestURL());

        return routeFactory.matchRoute(actionParam.getRequestURL());
    }
}
