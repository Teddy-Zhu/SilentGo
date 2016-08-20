package com.silentgo.core.route.support;

import com.silentgo.config.Const;
import com.silentgo.config.Regex;
import com.silentgo.core.route.BasicRoute;
import com.silentgo.core.route.RegexRoute;
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

    public Object[] matchRoute(String url) {
        Object[] ret = new Object[2];
        if (hashRoute.containsKey(url)) {
            ret[0] = hashRoute.get(url);
            return ret;
        }

        for (RegexRoute route : regexRoute) {
            Matcher matcher = route.getPattern().matcher(url);
            if (matcher.matches()) {
                ret[0] = route;
                ret[1] = matcher;
                return ret;
            }
        }
        return null;
    }

    @Override
    public String getName() {
        return Const.RouteFactory;
    }
}
