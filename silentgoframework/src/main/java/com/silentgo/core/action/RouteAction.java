package com.silentgo.core.action;

import com.silentgo.config.Const;
import com.silentgo.core.SilentGo;
import com.silentgo.core.action.annotation.Action;
import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.ioc.bean.BeanFactory;
import com.silentgo.core.route.BasicRoute;
import com.silentgo.core.route.RegexRoute;
import com.silentgo.core.route.support.RouteFactory;
import com.silentgo.core.route.support.RouteParamPaser;
import com.silentgo.kit.logger.Logger;
import com.silentgo.kit.logger.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;

/**
 * Project : silentgo
 * com.silentgo.core.action
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by  on 16/7/18.
 */
@Action
public class RouteAction extends ActionChain {

    public static final Logger LOGGER = LoggerFactory.getLog(RouteAction.class);

    @Override
    public int priority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void doAction(ActionParam param) {

        RouteFactory routeFactory = (RouteFactory) SilentGo.getInstance().getConfig().getFactory(Const.RouteFactory);

        BeanFactory beanFactory = (BeanFactory) SilentGo.getInstance().getConfig().getFactory(Const.BeanFactory);
        Object[] ret = routeFactory.matchRoute(param.getRequestURL());

        if (ret == null) {
            LOGGER.debug("can not match url {}", param.getRequestURL());
        } else {
            BasicRoute basicRoute = (BasicRoute) ret[0];
            Object returnVal = null;
            try {
                returnVal = basicRoute instanceof RegexRoute ?
                        regexRoute((RegexRoute) basicRoute, param, beanFactory, (Matcher) ret[1]) : baseRoute(basicRoute, param, beanFactory);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        param.setHandled(true);
    }


    private Object regexRoute(RegexRoute route, ActionParam param, BeanFactory beanFactory, Matcher matcher) throws InvocationTargetException {
        RouteParamPaser.parsePathVariable(param.getRequest(), route, matcher);
        return baseRoute(route, param, beanFactory);
    }

    private Object baseRoute(BasicRoute route, ActionParam param, BeanFactory beanFactory) throws InvocationTargetException {

        MethodAdviser adviser = route.getAdviser();

        Object[] args = RouteParamPaser.parseParams(adviser, param.getRequest(), param.getResponse());
        Object bean = beanFactory.getBean(adviser.getClassName()).getBean();
        return adviser.getMethod().invoke(bean, args);
    }

}
