package com.silentgo.servlet;

import com.silentgo.config.Const;
import com.silentgo.config.SilentGoConfig;
import com.silentgo.core.action.ActionChain;
import com.silentgo.core.action.ActionDispatcher;
import com.silentgo.core.action.ActionParam;
import com.silentgo.core.SilentGo;
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

    private SilentGoConfig config;

    private static SilentGo appContext = SilentGo.getInstance();

    public SilentGoFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (!appContext.isLoaded()) {

            LOGGER.info("Start SilentGo Loader ...");

            LOGGER.info("file.encoding = {}", System.getProperty("file.encoding"));

            long startTime = System.currentTimeMillis();

            this.config = appContext.getConfig();

            ServletContext context = filterConfig.getServletContext();


            String contextPath = context.getContextPath();
            int contextPathLength = contextPath == null || "/".equals(contextPath) ? 0 : contextPath.length();

            if (null == config) {
                String configClassName = filterConfig.getInitParameter("config");
                if (!StringKit.isNullOrEmpty(configClassName)) {
                    this.config = getConfig(configClassName);
                } else {
                    //noinspection unchecked
                    this.config = new SilentGoConfig(Const.BasePackages, Const.EmptyArray, true, Const.Encoding, contextPathLength);
                }
            }

            ActionChain actionChain = ActionDispatcher.getAction();

            config.setContextPathLength(contextPathLength);

            appContext.setActionChain(actionChain);
            appContext.setContext(context);
            appContext.setLoaded(true);
            LOGGER.info("SilentGo Loader initialize successfully, Time : {} ms.", System.currentTimeMillis() - startTime);
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Request request = new Request((HttpServletRequest) servletRequest);
        Response response = new Response((HttpServletResponse) servletResponse);

        request.setCharacterEncoding(config.getEncoding());

        String requestPath = request.getRequestURI();

        requestPath = config.getContextPathLength() == 0 ?
                requestPath : requestPath.substring(config.getContextPathLength());
        ActionParam param = new ActionParam(false, request, response, requestPath);

        try {
            appContext.getActionChain().doAction(param);
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

    private SilentGoConfig getConfig(String className) throws ServletException {
        SilentGoConfig configClass = null;
        try {
            Class<SilentGoConfig> applicationClass = (Class<SilentGoConfig>) Class.forName(className);

            configClass = applicationClass == null ? configClass : applicationClass.newInstance();

        } catch (Exception e) {
            throw new ServletException(e);
        }
        return configClass;
    }

}
