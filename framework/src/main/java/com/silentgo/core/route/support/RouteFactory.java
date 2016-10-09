package com.silentgo.core.route.support;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.aop.support.MethodAOPFactory;
import com.silentgo.core.build.Factory;
import com.silentgo.core.config.Const;
import com.silentgo.core.config.Regex;
import com.silentgo.core.exception.AppReleaseException;
import com.silentgo.core.ioc.bean.BeanFactory;
import com.silentgo.core.ioc.bean.BeanWrapper;
import com.silentgo.core.route.BasicRoute;
import com.silentgo.core.route.RegexRoute;
import com.silentgo.core.route.Route;
import com.silentgo.core.route.annotation.Controller;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.utils.StringKit;
import com.silentgo.utils.logger.Logger;
import com.silentgo.utils.logger.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Project : silentgo
 * com.silentgo.core.route
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by  on 16/7/18.
 */
@Factory
public class RouteFactory extends BaseFactory {

    private static final Logger LOGGER = LoggerFactory.getLog(RouteFactory.class);

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
     *
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

    /**
     * match all
     *
     * @param url
     * @return
     */
    public Map<Route, Double> matchRoutes(String url) {
        Map<Route, Double> routeMap = new HashMap<>();

        if (hashRoute.containsKey(url)) {
            routeMap.put(new Route(hashRoute.get(url), null), 1.5);
        }

        for (RegexRoute route : regexRoute) {
            Matcher matcher = route.getPattern().matcher(url);
            if (matcher.matches()) {
                routeMap.put(new Route(route, matcher), 1.0);
            }
        }
        return routeMap;
    }

    @Override
    public boolean initialize(SilentGo me) {

        //build route

        me.getAnnotationManager().getClasses(Controller.class).forEach(aClass -> buildClass(aClass, me));

        return true;
    }

    @Override
    public boolean destroy(SilentGo me) throws AppReleaseException {
        return false;
    }


    private void buildClass(Class<?> aClass, SilentGo me) {
        Controller controller = aClass.getAnnotation(Controller.class);
        com.silentgo.core.route.annotation.Route route = aClass.getAnnotation(com.silentgo.core.route.annotation.Route.class);
        boolean parentRegex = route != null && route.regex();
        String path = filterPath((route != null && !Const.DEFAULT_NONE.equals(route.value())) ? route.value() : aClass.getSimpleName(), true);
        BeanFactory beanFactory = me.getFactory(me.getConfig().getBeanClass());
        MethodAOPFactory methodAOPFactory = me.getFactory(MethodAOPFactory.class);
        BeanWrapper bean = beanFactory.getBean(aClass.getName());

        Pattern routePattern = Pattern.compile(Regex.RoutePath);

        //LOGGER.info("build route class:{}", aClass.getName());
        for (Method method : bean.getBeanClass().getJavaClass().getDeclaredMethods()) {
            MethodAdviser adviser = methodAOPFactory.getMethodAdviser(method);
            com.silentgo.core.route.annotation.Route an = adviser.getAnnotation(com.silentgo.core.route.annotation.Route.class);
            if (an == null) continue;

            String fullPath = mergePath(path, filterPath(Const.DEFAULT_NONE.equals(an.value()) ?
                    method.getName() : an.value(), true));
            Matcher matcher = routePattern.matcher(fullPath);

            if (an.regex() || parentRegex || matcher.find()) {
                addRoute(buildRegexRoute(fullPath, matcher, adviser));
            } else {
                addRoute(buildBasicRoute(fullPath, adviser));
            }
        }


    }

    private BasicRoute buildBasicRoute(String path, MethodAdviser adviser) {
        BasicRoute basicRoute = new BasicRoute();

        basicRoute.setAdviser(adviser);
        basicRoute.setPath(path);
        return basicRoute;
    }

    private RegexRoute buildRegexRoute(String path, Matcher matcher, MethodAdviser adviser) {
        BasicRoute basicRoute = buildBasicRoute(path, adviser);
        RegexRoute route = new RegexRoute(basicRoute);
        String resolvedMatch = path;
        resolvedMatch = resolveRoute(matcher, route, resolvedMatch, path);
        while (matcher.find()) {
            resolvedMatch = resolveRoute(matcher, route, resolvedMatch, path);
        }
        route.setPattern(Pattern.compile(resolvedMatch));
        return route;
    }

    private String resolveRoute(Matcher matcher, RegexRoute route, String resolvedMatch, String path) {
        String rule = matcher.group();
        String ruleSolved = rule.substring(1, rule.length() - 1);
        if (ruleSolved.contains(Regex.RouteSplit)) {
            if (ruleSolved.length() > Regex.RouteSplit.length()) {
                String name = StringKit.getLeft(ruleSolved, Regex.RouteSplit).trim();
                String regex = StringKit.getRight(ruleSolved, Regex.RouteSplit).trim();
                regex = StringKit.isBlank(regex) ? Regex.RegexAll : regex;
                boolean needName = !StringKit.isBlank(name);
                String replacement = needName ? Regex.RoutePathNameRegexMatch : Regex.RoutePathCustomMatch;
                if (needName) {
                    route.addName(name);
                    replacement = String.format(replacement, name, regex);
                    resolvedMatch = replaceRegex(resolvedMatch, rule, replacement);
                } else {
                    replacement = String.format(replacement, regex);
                    resolvedMatch = replaceRegex(resolvedMatch, rule, replacement);
                }


            } else {
                LOGGER.warn("can not match rule {} in path {}, ignored !", rule, path);
            }
        } else {
            route.addName(ruleSolved);
            resolvedMatch = replaceRegex(resolvedMatch, rule, String.format(Regex.RoutePathNameRegexMatch, ruleSolved, Regex.RegexAll));

        }
        return resolvedMatch;
    }

    private String replaceRegex(String source, String target, String replacement) {
        return source.replace(target, replacement);
    }

    private String mergePath(String suffix, String prefix) {
        suffix = filterPath(suffix, true);
        suffix = suffix.equals(Const.Slash) ? suffix : (suffix + Const.Slash);
        return suffix + (prefix.startsWith(Const.Slash) ? prefix.substring(1) : prefix);
    }

    private String filterPath(String path, boolean end) {
        if (path.equals(Const.Slash)) return path;
        if (path.endsWith(Const.Slash) && end) path = path.substring(0, path.length() - 1);
        if (path.startsWith(Const.Slash)) return path;
        return Const.Slash + path;
    }
}
