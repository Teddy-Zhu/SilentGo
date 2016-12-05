package com.silentgo.servlet;

import com.silentgo.core.SilentGo;
import com.silentgo.core.action.ActionParam;
import com.silentgo.core.build.Factory;
import com.silentgo.core.config.Config;
import com.silentgo.core.config.Const;
import com.silentgo.core.config.SilentGoConfig;
import com.silentgo.core.config.support.ConfigChecker;
import com.silentgo.core.exception.AppBuildException;
import com.silentgo.core.exception.AppReleaseException;
import com.silentgo.core.ioc.bean.BeanFactory;
import com.silentgo.core.render.support.ErrorRener;
import com.silentgo.core.support.AnnotationManager;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.servlet.http.HttpStatus;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;
import com.silentgo.utils.StringKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by teddyzhu on 16/7/15.
 */
public class SilentGoFilter implements Filter {

    private static Logger LOGGER = LoggerFactory.getLogger(SilentGoFilter.class);

    private static Config configInit = null;

    private static SilentGo appContext = SilentGo.me();

    private SilentGoConfig globalConfig = null;

    public SilentGoFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        SilentGoConfig config = null;
        long startTime = System.currentTimeMillis();
        LOGGER.info("SilentGoConfig filter initialize");

        ServletContext context = filterConfig.getServletContext();

        String contextPath = context.getContextPath();
        int contextPathLength = contextPath == null || Const.Slash.equals(contextPath) ? 0 : contextPath.length();

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

        //scan packages and jars
        AnnotationManager manager = new AnnotationManager(config);
        appContext.setAnnotationManager(manager);

        if (configInit != null) {
            LOGGER.debug("init default build");
            configInit.initialBuild(config);
        }
        LOGGER.debug("init extra build");
        for (Config extraConfig : config.getExtraConfig()) {
            extraConfig.initialBuild(config);
        }

        //build bean first
        LOGGER.debug("init bean factory");
        initBeanFactory(config);

        LOGGER.debug("init other factorys");
        buildFactory(manager, config);

        if (configInit != null) {
            LOGGER.debug("after init default config");
            configInit.afterInit(config);
        }

        LOGGER.debug("after init extra config");
        for (Config extraConfig : config.getExtraConfig()) {
            extraConfig.afterInit(config);
        }


        ConfigChecker.Check(config);

        globalConfig = appContext.getConfig();

        LOGGER.info("SilentGoConfig filter initialize successfully, Time : {} ms.", System.currentTimeMillis() - startTime);

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

        ActionParam param = new ActionParam(request, response, requestPath, filterChain);
        globalConfig.getCtx().set(new SilentGoContext(param));
        try {
            long start = System.currentTimeMillis();
            LOGGER.debug("action {} start", requestPath);
            globalConfig.getActionChain().doAction(param);
            LOGGER.debug("action {} end in : {} ms", requestPath, System.currentTimeMillis() - start);
        } catch (Throwable throwable) {
            LOGGER.error("action error", throwable);
            new ErrorRener().render(request, response, HttpStatus.Code.INTERNAL_SERVER_ERROR, throwable, appContext.isDevMode());
            return;
        } finally {
            globalConfig.getCtx().remove();
        }
    }


    @Override
    public void destroy() {
        LOGGER.info("close filter");
        globalConfig.getFactoryMap()
                .forEach((k, v) -> {
                    try {
                        v.destroy(appContext);
                    } catch (AppReleaseException e) {
                        e.printStackTrace();
                    }
                });
    }

    private void buildFactory(AnnotationManager manager, SilentGoConfig config) {

        //add builder
        manager.getClasses(Factory.class).forEach(factory -> {
            if (!factory.isInterface() && BaseFactory.class.isAssignableFrom(factory)) {
                appContext.getFactory(factory);
            }
        });
        config.getFactories().forEach(factory -> {
            if (!factory.isInterface()) {
                appContext.getFactory(factory);
            }
        });
    }

    private void initBeanFactory(SilentGoConfig config) {
        LOGGER.info("build bean Factory");
        BeanFactory beanFactory = null;
        try {
            beanFactory = config.getBeanClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("init bean instance error", e);
        }
        config.addFactory(beanFactory);
        beanFactory.addBean(config, true, false, false);
        try {
            beanFactory.initialize(appContext);
        } catch (AppBuildException e) {
            e.printStackTrace();
            LOGGER.error("init bean error", e);
        }
        beanFactory.addBean(beanFactory, true, false, false);

    }

    private Config getConfig(String className) throws ServletException {
        Config configInit = null;
        try {
            Class configInitClass = Class.forName(className);

            if (Config.class.isAssignableFrom(configInitClass)) {
                configInit = (Config) configInitClass.newInstance();
            }

        } catch (Exception e) {
            LOGGER.error("get config error", e);
            throw new ServletException(e);
        }
        return configInit == null ? new Config() {
        } : configInit;
    }

}
