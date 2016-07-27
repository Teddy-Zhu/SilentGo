package com.silentgo.servlet;

import com.silentgo.config.Config;
import com.silentgo.config.Const;
import com.silentgo.config.SilentGoConfig;
import com.silentgo.core.action.ActionChain;
import com.silentgo.core.action.support.ActionDispatcher;
import com.silentgo.core.action.ActionParam;
import com.silentgo.core.SilentGo;
import com.silentgo.core.ioc.bean.support.BeanBuilder;
import com.silentgo.core.support.AnnotationManager;
import com.silentgo.kit.StringKit;
import com.silentgo.logger.Logger;
import com.silentgo.logger.LoggerFactory;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * Created by teddyzhu on 16/7/15.
 */
public class SilentGoFilter implements Filter {

    private static Logger LOGGER = LoggerFactory.getLog(SilentGoFilter.class);

    private Config configInit;

    private static SilentGo appContext = SilentGo.getInstance();

    private SilentGoConfig globalConfig;

    public SilentGoFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (!appContext.isLoaded()) {
            SilentGoConfig config = null;

            LOGGER.info("Start SilentGo Init ...");
            LOGGER.info("file.encoding = {}", System.getProperty("file.encoding"));

            long startTime = System.currentTimeMillis();

            ServletContext context = filterConfig.getServletContext();

            String contextPath = context.getContextPath();
            int contextPathLength = contextPath == null || "/".equals(contextPath) ? 0 : contextPath.length();

            //noinspection unchecked
            config = new SilentGoConfig(Const.BasePackages, Const.EmptyArray, true, Const.Encoding, contextPathLength);

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

            //Init Bean
            BeanBuilder.Build(appContext);

            //Init
            ActionChain actionChain = ActionDispatcher.getAction();

            config.setActionChain(actionChain);


            configInit.afterInit(config);

            globalConfig = appContext.getConfig();

            appContext.setLoaded(true);


            LOGGER.info("SilentGo Loader initialize successfully, Time : {} ms.", System.currentTimeMillis() - startTime);
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
        ActionParam param = new ActionParam(false, request, response, requestPath);

        try {
            globalConfig.getActionChain().doAction(param);
        } catch (Exception e) {

        }

        if (!param.isHandled())
            filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }


    private File getWebRoot(ServletContext sc) {
        String dir = sc.getRealPath("/");
        if (dir == null) {
            try {
                URL url = sc.getResource("/");
                if (url != null && "file".equals(url.getProtocol())) {
                    dir = URLDecoder.decode(url.getFile(), "utf-8");
                } else {
                    throw new IllegalStateException("Can't get webroot dir, url = " + url);
                }
            } catch (MalformedURLException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return new File(dir);
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
