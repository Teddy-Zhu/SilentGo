package com.silentgo.config;

/**
 * Project : silentgo
 * com.silentgo.config
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/18.
 */
public class Regex {
    public static final String RoutePath = "\\{(\\w*)(?:.*?)\\}";

    public static final String RegexAll = ".*?";

    public static final String RoutePathCustomMatch = "(%s)";
    public static final String RoutePathNameRegexMatch = "(?<%s>%s)";

    public static final String RouteSplit = ":";

}
