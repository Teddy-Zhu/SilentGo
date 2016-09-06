package com.silentgo.core.action;

import com.silentgo.core.SilentGo;
import com.silentgo.core.action.annotation.Action;
import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.exception.AppException;
import com.silentgo.core.exception.AppParameterResolverException;
import com.silentgo.core.exception.AppRenderException;
import com.silentgo.core.exception.support.ExceptionFactory;
import com.silentgo.core.exception.support.ExceptionKit;
import com.silentgo.core.ioc.bean.BeanFactory;
import com.silentgo.core.render.RenderModel;
import com.silentgo.core.render.renderresolver.RenderResolver;
import com.silentgo.core.render.renderresolver.RenderResolverFactory;
import com.silentgo.core.render.support.ErrorRener;
import com.silentgo.core.render.support.RenderFactory;
import com.silentgo.core.route.ParameterDispatcher;
import com.silentgo.core.route.Route;
import com.silentgo.core.route.support.annotationResolver.ParamAnFactory;
import com.silentgo.core.route.support.annotationResolver.ParamAnnotationResolver;
import com.silentgo.core.route.support.paramdispatcher.MultiPartDispatch;
import com.silentgo.core.route.support.paramdispatcher.ParamDispatchFactory;
import com.silentgo.core.route.support.RouteFactory;
import com.silentgo.core.route.support.paramvalueresolve.ParameterResolveFactory;
import com.silentgo.kit.logger.Logger;
import com.silentgo.kit.logger.LoggerFactory;
import com.silentgo.servlet.http.HttpStatus;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

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
    public void doAction(ActionParam param) throws Exception {

        Response response = param.getResponse();

        Request request = param.getRequest();
        SilentGo me = SilentGo.getInstance();

        boolean isDev = me.isDevMode();

        RouteFactory routeFactory = me.getFactory(RouteFactory.class);

        Route ret = me.getConfig().getRoutePaser().praseRoute(routeFactory, param);
        param.setHandled(true);
        if (ret == null) {
            LOGGER.debug("can not match url {}", param.getRequestURL());
            new ErrorRener().render(request, response, HttpStatus.Code.NOT_FOUND, null, isDev);
        } else {
            BeanFactory beanFactory = me.getFactory(BeanFactory.class);

            ParamDispatchFactory paramDispatchFactory = me.getFactory(ParamDispatchFactory.class);

            ParameterResolveFactory parameterResolveFactory = me.getFactory(ParameterResolveFactory.class);

            ParamAnFactory paramAnFactory = me.getFactory(ParamAnFactory.class);
            RenderResolverFactory renderResolverFactory = me.getFactory(RenderResolverFactory.class);
            RenderFactory renderFactory = me.getFactory(RenderFactory.class);

            MethodAdviser adviser = ret.getRoute().getAdviser();
            Object[] args = new Object[adviser.getParams().length];
            Object bean = beanFactory.getBean(adviser.getClassName()).getBean();

            try {
                if (!paramAnFactory.resolve(adviser, request, response)) {
                    new ErrorRener().render(request, response, HttpStatus.Code.METHOD_NOT_ALLOWED, null, isDev);
                    return;
                }

                // parameter dispatch
                paramDispatchFactory.dispatch(parameterResolveFactory, param, ret, args);


                Object returnVal = null;

                //controller method with interceptors
                returnVal = adviser.getMethod().invoke(bean, args);

                //render
                renderResolverFactory.render(renderFactory, adviser, request, response, returnVal);

            } catch (AppException e) {
                new ErrorRener().render(request, response, e, isDev);
            } catch (Exception ex) {
                //exception handle
                ExceptionFactory exceptionFactory = me.getFactory(ExceptionFactory.class);

                exceptionFactory.handle(renderResolverFactory, renderFactory, beanFactory, adviser, request, response, ex);

            } finally {
                paramDispatchFactory.release(param);
            }

        }
    }

}
