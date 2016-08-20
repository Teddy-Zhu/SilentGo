package com.silentgo.core.route.support;

import com.silentgo.build.SilentGoBuilder;
import com.silentgo.build.annotation.Builder;
import com.silentgo.config.Const;
import com.silentgo.config.Regex;
import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.aop.support.MethodAOPFactory;
import com.silentgo.core.ioc.bean.BeanFactory;
import com.silentgo.core.ioc.bean.BeanWrapper;
import com.silentgo.core.route.BasicRoute;
import com.silentgo.core.route.RegexRoute;
import com.silentgo.core.route.annotation.Controller;
import com.silentgo.core.route.annotation.Route;
import com.silentgo.kit.StringKit;
import com.silentgo.kit.logger.Logger;
import com.silentgo.kit.logger.LoggerFactory;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Project : silentgo
 * com.silentgo.core.action.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/25.
 */
@Builder
public class RouteBuilder extends SilentGoBuilder {


    @Override
    public Integer priority() {
        return 30;
    }

    private static final Logger LOGGER = LoggerFactory.getLog(RouteBuilder.class);

    private void buildClass(Class<?> aClass, SilentGo me, RouteFactory routeFactory) {
        Controller controller = aClass.getAnnotation(Controller.class);
        String path = filterPath(controller.value().equals(Const.DEFAULT_NONE) ? aClass.getSimpleName() : controller.value(), true);
        BeanFactory beanFactory = (BeanFactory) me.getConfig().getFactory(Const.BeanFactory);
        MethodAOPFactory methodAOPFactory = (MethodAOPFactory) me.getConfig().getFactory(Const.MethodAOPFactory);
        BeanWrapper bean = beanFactory.getBean(aClass.getName());

        Pattern routePattern = Pattern.compile(Regex.RoutePath);

        for (Method method : bean.getBeanClass().getJavaClass().getDeclaredMethods()) {
            String methodName = aClass.getName() + "." + method.getName();
            MethodAdviser adviser = methodAOPFactory.getMethodAdviser(methodName);
            Route an = adviser.getAnnotation(Route.class);
            if (an == null) continue;

            String fullPath = path + filterPath(Const.DEFAULT_NONE.equals(an.value()) ?
                    method.getName() : an.value(), true);

            Matcher matcher = routePattern.matcher(path);

            if (an.regex() || matcher.matches()) {
                routeFactory.addRoute(buildRegexRoute(fullPath, matcher, adviser));
            } else {
                routeFactory.addRoute(buildBasicRoute(fullPath, adviser));
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
        while (matcher.find()) {
            String rule = matcher.group();
            String ruleSolved = rule.substring(1, rule.length() - 1);
            if (ruleSolved.contains(Regex.RouteSplit)) {
                if (ruleSolved.length() > Regex.RouteSplit.length()) {
                    String name = StringKit.getLeft(ruleSolved, Regex.RouteSplit).trim();
                    String regex = StringKit.getRight(ruleSolved, Regex.RouteSplit).trim();
                    regex = StringKit.isNullOrEmpty(regex) ? Regex.RegexAll : regex;
                    boolean needName = StringKit.isNullOrEmpty(regex);
                    String replacement = needName ? Regex.RoutePathNameRegexMatch : Regex.RoutePathCustomMatch;
                    if (needName) {
                        route.addName(name);
                        replacement = String.format(replacement, name, regex);
                    } else {
                        replacement = String.format(replacement, regex);
                    }
                    resolvedMatch = replaceRegex(resolvedMatch, rule, replacement);

                } else {
                    LOGGER.warn("can not match rule {} in path {}, ignored !", rule, path);
                }
            } else {
                route.addName(ruleSolved);
                resolvedMatch = replaceRegex(resolvedMatch, rule, String.format(Regex.RoutePathCustomMatch, Regex.RegexAll));

            }
        }
        route.setPattern(Pattern.compile(resolvedMatch));
        return route;
    }

    private String replaceRegex(String source, String target, String replacement) {
        return source.replace(target, replacement);
    }

    private String filterPath(String path, boolean end) {
        if (path.endsWith("/") && end) path = path.substring(0, path.length() - 1);
        if (path.startsWith("/")) return path;
        return "/" + path;
    }

    @Override
    public boolean build(SilentGo me) {

        RouteFactory routeFactory = new RouteFactory();
        me.getConfig().addFactory(routeFactory);
        me.getAnnotationManager().getClasses(Controller.class).forEach(aClass -> buildClass(aClass, me, routeFactory));
        return true;
    }
}
