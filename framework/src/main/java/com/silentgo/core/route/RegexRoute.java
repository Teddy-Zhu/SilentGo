package com.silentgo.core.route;

import com.silentgo.core.aop.MethodAdviser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Project : silentgo
 * com.silentgo.core.route
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/18.
 */
public class RegexRoute extends BasicRoute {
    private Pattern pattern;

    private List<String> names;

    public RegexRoute() {
        names = new ArrayList<>();
    }


    public RegexRoute(BasicRoute basicRoute) {
        setAdviser(basicRoute.getAdviser());
        setPath(basicRoute.getPath());
        names = new ArrayList<>();
    }

    public RegexRoute(Pattern pattern, List<String> names) {
        this.pattern = pattern;
        this.names = names;
    }

    public RegexRoute(String path, MethodAdviser adviser, Pattern pattern, List<String> names) {
        super(path, adviser);
        this.pattern = pattern;
        this.names = names;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public boolean addName(String name) {
        return names.add(name);
    }
}
