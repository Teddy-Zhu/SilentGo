package com.silentgo.core.route;

import com.silentgo.core.action.ActionParam;
import com.silentgo.core.route.support.RouteFactory;

/**
 * Project : silentgo
 * com.silentgo.core.route.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/22.
 */
public interface RoutePaser {

    public Route praseRoute(RouteFactory routeFactory, ActionParam actionParam);
}
