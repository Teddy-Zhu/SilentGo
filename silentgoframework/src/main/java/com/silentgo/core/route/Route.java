package com.silentgo.core.route;

import java.util.regex.Matcher;

/**
 * Project : silentgo
 * com.silentgo.core.route
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/22.
 */
public class Route {

    private BasicRoute route;
    private boolean isRegex;

    private Matcher matcher;

    public BasicRoute getRoute() {
        return route;
    }

    public RegexRoute getRegexRoute() {
        return isRegex ? (RegexRoute) route : null;
    }

    public void setRoute(BasicRoute route) {
        this.route = route;
    }

    public boolean isRegex() {
        return isRegex;
    }

    public void setRegex(boolean regex) {
        isRegex = regex;
    }

    public Matcher getMatcher() {
        return isRegex ? matcher : null;
    }

    public void setMatcher(Matcher matcher) {
        this.matcher = matcher;
    }

    public Route(BasicRoute route, Matcher matcher) {
        this.route = route;
        this.isRegex = route instanceof RegexRoute;
        this.matcher = matcher;
    }
}
