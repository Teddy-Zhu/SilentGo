package com.silentgo.core.action;

import com.silentgo.core.config.Const;
import com.silentgo.core.SilentGo;
import com.silentgo.core.action.annotation.Action;
import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.exception.AppRenderException;
import com.silentgo.core.ioc.bean.BeanFactory;
import com.silentgo.core.route.BasicRoute;
import com.silentgo.core.route.RegexRoute;
import com.silentgo.core.route.Route;
import com.silentgo.core.route.support.ParamDispatchFactory;
import com.silentgo.core.route.support.RouteFactory;
import com.silentgo.core.route.support.paramresolve.ParameterResolveFactory;
import com.silentgo.kit.logger.Logger;
import com.silentgo.kit.logger.LoggerFactory;

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
    public Integer priority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void doAction(ActionParam param) {
        SilentGo me = SilentGo.getInstance();
        RouteFactory routeFactory = me.getFactory(RouteFactory.class);

        Route ret = me.getConfig().getRoutePaser().praseRoute(routeFactory, param);

        if (ret == null) {
            LOGGER.debug("can not match url {}", param.getRequestURL());
        } else {

            BeanFactory beanFactory = me.getFactory(BeanFactory.class);

            ParamDispatchFactory paramDispatchFactory = me.getFactory(ParamDispatchFactory.class);

            ParameterResolveFactory parameterResolveFactory = me.getFactory(ParameterResolveFactory.class);

            MethodAdviser adviser = ret.getRoute().getAdviser();
            Object[] args = new Object[adviser.getParams().length];
            Object bean = beanFactory.getBean(adviser.getClassName()).getBean();

            // parameter dispatch
            paramDispatchFactory.getDispatchers().forEach(parameterDispatcher -> parameterDispatcher.dispatch(parameterResolveFactory, param, ret, args));

            Object returnVal = null;
            try {
                //controller method with interceptors
                returnVal = adviser.getMethod().invoke(bean, args);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            //render
            try {
                me.getConfig().getRender().render(ret, param.getResponse(), param.getRequest(), returnVal);
            } catch (AppRenderException e) {
                LOGGER.error("Render [{}] error : {}", me.getConfig().getRender().getClass(), e.getMessage());
            }

        }
        param.setHandled(true);
    }

}
