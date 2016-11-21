package com.silentgo.core.action;

import com.silentgo.core.SilentGo;
import com.silentgo.core.action.annotation.Action;
import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.exception.AppException;
import com.silentgo.core.exception.support.ExceptionFactory;
import com.silentgo.core.ioc.bean.BeanFactory;
import com.silentgo.core.render.renderresolver.RenderResolverFactory;
import com.silentgo.core.render.support.ErrorRener;
import com.silentgo.core.render.support.RenderFactory;
import com.silentgo.core.route.Route;
import com.silentgo.core.route.support.RouteFactory;
import com.silentgo.core.route.support.annotationresolver.RouteAnFactory;
import com.silentgo.core.route.support.paramdispatcher.ParamDispatchFactory;
import com.silentgo.core.route.support.paramresolver.ParameterResolveFactory;
import com.silentgo.core.route.support.requestdispatch.RequestDispatchFactory;
import com.silentgo.servlet.http.HttpStatus;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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

    public static final Logger LOGGER = LoggerFactory.getLogger(RouteAction.class);

    @Override
    public Integer priority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void doAction(ActionParam param) throws Exception {

        LOGGER.info("enter route action");

        Response response = param.getResponse();

        Request request = param.getRequest();
        SilentGo me = SilentGo.me();

        boolean isDev = me.isDevMode();

        RouteFactory routeFactory = me.getFactory(RouteFactory.class);

        RequestDispatchFactory requestDispatchFactory = me.getFactory(RequestDispatchFactory.class);
        long start = System.currentTimeMillis();

        requestDispatchFactory.dispatch(param);
        LOGGER.info("dispatch route end in :{} ", System.currentTimeMillis() - start);

        Route ret = me.getConfig().getRoutePaser().praseRoute(routeFactory, param);

        if (ret == null) {
            LOGGER.debug("can not match url {}", param.getRequestURL());
            new ErrorRener().render(request, response, HttpStatus.Code.NOT_FOUND, null, isDev);
        } else {
            LOGGER.info("find route : {}", ret.getRoute().getPath());
            BeanFactory beanFactory = me.getFactory(me.getConfig().getBeanClass());

            ParamDispatchFactory paramDispatchFactory = me.getFactory(ParamDispatchFactory.class);

            ParameterResolveFactory parameterResolveFactory = me.getFactory(ParameterResolveFactory.class);

            RouteAnFactory paramAnFactory = me.getFactory(RouteAnFactory.class);
            RenderResolverFactory renderResolverFactory = me.getFactory(RenderResolverFactory.class);
            RenderFactory renderFactory = me.getFactory(RenderFactory.class);

            MethodAdviser adviser = ret.getRoute().getAdviser();
            Object[] args = new Object[adviser.getParams().length];
            Object bean = beanFactory.getBean(adviser.getClassName()).getObject();

            try {
                start = System.currentTimeMillis();
                if (!paramAnFactory.resolve(adviser, request, response)) {
                    new ErrorRener().render(request, response, HttpStatus.Code.METHOD_NOT_ALLOWED, null, isDev);
                    return;
                }
                LOGGER.info("paramAnFactory resolve end in :{} ", System.currentTimeMillis() - start);

                start = System.currentTimeMillis();
                // parameter dispatch
                paramDispatchFactory.dispatch(parameterResolveFactory, param, ret, args);
                LOGGER.info("paramDispatchFactory dispatch end in :{} ", System.currentTimeMillis() - start);


                Object returnVal = null;

                start = System.currentTimeMillis();
                //controller method with interceptors
                returnVal = adviser.getMethod().invoke(bean, args);
                LOGGER.info("route method end in :{} ", System.currentTimeMillis() - start);
                start = System.currentTimeMillis();
                //render
                renderResolverFactory.render(renderFactory, adviser, request, response, returnVal);
                LOGGER.info("render method end in :{} ", System.currentTimeMillis() - start);
            } catch (AppException e) {
                LOGGER.error("route action AppException error", e);
                new ErrorRener().render(request, response, e, isDev);
            } catch (Exception ex) {
                LOGGER.error("route action other error", ex);
                //exception handle
                ExceptionFactory exceptionFactory = me.getFactory(ExceptionFactory.class);

                exceptionFactory.handle(renderResolverFactory, renderFactory, beanFactory, adviser, request, response, ex);

            } finally {
                requestDispatchFactory.release(param);
            }

        }
    }

}
