package com.silentgo.servlet;

import com.silentgo.core.build.SilentGoBuilder;
import com.silentgo.core.build.annotation.Builder;
import com.silentgo.core.config.Config;
import com.silentgo.core.config.Const;
import com.silentgo.core.config.SilentGoConfig;
import com.silentgo.core.SilentGo;
import com.silentgo.core.action.ActionParam;
import com.silentgo.core.exception.AppBuildException;
import com.silentgo.core.exception.support.ExceptionFactory;
import com.silentgo.core.render.support.ErrorRener;
import com.silentgo.core.support.AnnotationManager;
import com.silentgo.kit.SilentGoContext;
import com.silentgo.kit.StringKit;
import com.silentgo.kit.logger.Logger;
import com.silentgo.kit.logger.LoggerFactory;
import com.silentgo.servlet.http.HttpStatus;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by teddyzhu on 16/7/15.
 */
public class SilentGoFilter implements Filter {

    private static Logger LOGGER = LoggerFactory.getLog(SilentGoFilter.class);

    private Config configInit;

    private static com.silentgo.core.SilentGo appContext = com.silentgo.core.SilentGo.getInstance();

    private SilentGoConfig globalConfig;

    public SilentGoFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (!appContext.isLoaded()) {
            SilentGoConfig config = null;
            long startTime = System.currentTimeMillis();
            LOGGER.info("Start SilentGoConfig Init ...");

            ServletContext context = filterConfig.getServletContext();

            String contextPath = context.getContextPath();
            int contextPathLength = contextPath == null || "/".equals(contextPath) ? 0 : contextPath.length();

            //noinspection unchecked
            config = new SilentGoConfig(Const.BasePackages, Const.EmptyArray, true, Const.Encoding, contextPathLength, Const.configName);

            appContext.setConfig(config);
            String configClassName = filterConfig.getInitParameter("config");
            if (!StringKit.isNullOrEmpty(configClassName)) {
                configInit = getConfig(configClassName);
            } else {
                LOGGER.warn("Config class can not be found , the application may be run unnromally");
            }

            configInit.init(config);

            config.setContextPathLength(contextPathLength);

            appContext.setContext(context);

            AnnotationManager manager = new AnnotationManager(config);
            appContext.setAnnotationManager(manager);

            build(manager, config);

            configInit.afterInit(config);

            globalConfig = appContext.getConfig();

            appContext.setLoaded(true);

            LOGGER.info("SilentGoConfig Loader initialize successfully, Time : {} ms.", System.currentTimeMillis() - startTime);
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
        SilentGo.getInstance().getConfig().getCtx().set(new SilentGoContext(response, request));
        try {
            globalConfig.getActionChain().doAction(param);
        } catch (Throwable throwable) {
            new ErrorRener().render(request, response, HttpStatus.Code.INTERNAL_SERVER_ERROR, throwable, appContext.isDevMode());
        } finally {
            SilentGo.getInstance().getConfig().getCtx().remove();
        }

        if (!param.isHandled())
            filterChain.doFilter(request, response);
    }


    @Override
    public void destroy() {

    }

    private void build(AnnotationManager manager, SilentGoConfig config) {
        //add builder
        SilentGoConfig finalConfig = config;
        manager.getClasses(Builder.class).forEach(builder -> {
            try {
                if (SilentGoBuilder.class.isAssignableFrom(builder)) {
                    finalConfig.getBuilders().add((SilentGoBuilder) builder.newInstance());
                } else {
                    LOGGER.debug("SilentGoBuilder Class {} is invalid, it should be extend {}", builder.getClass().getName(), SilentGoBuilder.class.getName());
                }
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        config.getBuilders().sort((o1, o2) -> {
            int x = o1.priority();
            int y = o2.priority();
            return (x < y) ? -1 : ((x == y) ? 0 : 1);
        });

        //Init
        try {
            for (SilentGoBuilder silentGoBuilder : config.getBuilders()) {
                if (!silentGoBuilder.build(appContext)) {
                    LOGGER.warn("Builder {} build progress failed", silentGoBuilder.getClass().getName());
                }
            }
        } catch (AppBuildException e) {

        }


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
