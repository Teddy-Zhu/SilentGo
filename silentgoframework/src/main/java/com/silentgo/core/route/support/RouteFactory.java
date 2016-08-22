package com.silentgo.core.route.support;

import com.silentgo.core.config.Const;
import com.silentgo.core.config.Regex;
import com.silentgo.core.route.BasicRoute;
import com.silentgo.core.route.RegexRoute;
import com.silentgo.core.route.Route;
import com.silentgo.core.support.BaseFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * Project : silentgo
 * com.silentgo.core.route
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by  on 16/7/18.
 */
public class RouteFactory implements BaseFactory {

    private Map<String, BasicRoute> hashRoute = new HashMap<>();

    private List<RegexRoute> regexRoute = new ArrayList<>();


    public void addRoute(BasicRoute route) {
        if (route.getClass().equals(RegexRoute.class)) {
            regexRoute.add((RegexRoute) route);
        } else {
            hashRoute.put(route.getPath(), route);
        }
    }

    /**
     * simple match route
     * @param url
     * @return
     */
    public Route matchRoute(String url) {
        if (hashRoute.containsKey(url)) {
            return new Route(hashRoute.get(url), null);
        }

        for (RegexRoute route : regexRoute) {
            Matcher matcher = route.getPattern().matcher(url);
            if (matcher.matches()) {
                return new Route(route, matcher);
            }
        }
        return null;
    }

}
