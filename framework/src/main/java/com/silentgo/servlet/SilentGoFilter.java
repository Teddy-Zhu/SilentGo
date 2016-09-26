package com.silentgo.servlet;

import com.silentgo.core.SilentGo;
import com.silentgo.core.action.ActionParam;
import com.silentgo.core.build.Factory;
import com.silentgo.core.config.Config;
import com.silentgo.core.config.Const;
import com.silentgo.core.config.SilentGoConfig;
import com.silentgo.core.config.support.ConfigChecker;
import com.silentgo.core.exception.AppReleaseException;
import com.silentgo.core.render.support.ErrorRener;
import com.silentgo.core.support.AnnotationManager;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.servlet.http.HttpStatus;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;
import com.silentgo.utils.StringKit;
import com.silentgo.utils.logger.Logger;
import com.silentgo.utils.logger.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by teddyzhu on 16/7/15.
 */
public class SilentGoFilter implements Filter {

    private static Logger LOGGER = LoggerFactory.getLog(SilentGoFilter.class);

    private Config configInit = null;

    private static SilentGo appContext = SilentGo.getInstance();

    private SilentGoConfig globalConfig = null;

    public SilentGoFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (!appContext.isLoaded()) {
            SilentGoConfig config = null;
            long startTime = System.currentTimeMillis();
            LOGGER.info("start SilentGoConfig initial ...");

            ServletContext context = filterConfig.getServletContext();

            String contextPath = context.getContextPath();
            int contextPathLength = contextPath == null || "/".equals(contextPath) ? 0 : contextPath.length();

            //noinspection unchecked
            config = new SilentGoConfig(Const.BasePackages, Const.EmptyArray, true, Const.Encoding, contextPathLength, Const.configName);

            appContext.setConfig(config);
            String configClassName = filterConfig.getInitParameter("config");
            if (!StringKit.isBlank(configClassName)) {
                configInit = getConfig(configClassName);
            } else {
                LOGGER.warn("Config class can not be found , the application may be run unnromally");
            }
            config.setContextPathLength(contextPathLength);

            appContext.setContext(context);

            if (configInit != null) {
                configInit.init(config);
            }

            AnnotationManager manager = new AnnotationManager(config);
            appContext.setAnnotationManager(manager);

            build(manager, config);

            if (configInit != null) {
                configInit.afterInit(config);
            }

            ConfigChecker.Check(config);

            globalConfig = appContext.getConfig();

            appContext.setLoaded(true);

            LOGGER.info("SilentGoConfig filter initialize successfully, Time : {} ms.", System.currentTimeMillis() - startTime);
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Request request = new Request((HttpServletRequest) servletRequest);
        Response response = new Response((HttpServletResponse) servletResponse);

        request.setCharacterEncoding(globalConfig.getEncoding());

        String requestPath = request.getRequestURI();

        requestPath = globalConfig.getContextPathLength() == 0 ?
                requestPath : requestPath.substring(globalConfig.getContextPathLength());

        requestPath = requestPath.endsWith("/") ? requestPath.substring(0, requestPath.length() - 1) : requestPath;
        requestPath = requestPath.length() == 0 ? Const.Slash : requestPath;

        ActionParam param = new ActionParam(false, request, response, requestPath);
        globalConfig.getCtx().set(new SilentGoContext(response, request));
        try {
            LOGGER.info("start action");
            globalConfig.getActionChain().doAction(param);
            LOGGER.info("end action");
        } catch (Throwable throwable) {
            new ErrorRener().render(request, response, HttpStatus.Code.INTERNAL_SERVER_ERROR, throwable, appContext.isDevMode());
            return;
        } finally {
            globalConfig.getCtx().remove();
            LOGGER.info("final action");
        }

        if (!param.isHandled())
            filterChain.doFilter(request, response);
    }


    @Override
    public void destroy() {
        LOGGER.info("destroy filter");
        globalConfig.getFactoryMap()
                .forEach((k, v) -> {
                    try {
                        v.destroy(appContext);
                    } catch (AppReleaseException e) {
                        e.printStackTrace();
                    }
                });
    }

    private void build(AnnotationManager manager, SilentGoConfig config) {
        //add builder
        manager.getClasses(Factory.class).stream().forEach(factory -> {
            if (!factory.isInterface() && BaseFactory.class.isAssignableFrom(factory)) {
                appContext.getFactory(factory);
            }
        });
    }

    private Config getConfig(String className) throws ServletException {
        Config configInit = null;
        try {
            Class configInitClass = Class.forName(className);

            if (Config.class.isAssignableFrom(configInitClass)) {
                configInit = (Config) configInitClass.newInstance();
            }

        } catch (Exception e) {
            throw new ServletException(e);
        }
        return configInit == null ? new Config() {
        } : configInit;
    }

}
