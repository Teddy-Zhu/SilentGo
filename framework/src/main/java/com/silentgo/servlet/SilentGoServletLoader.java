package com.silentgo.servlet;

import com.silentgo.config.SilentGoConfig;
import com.silentgo.core.SilentGo;
import com.silentgo.core.SilentGoContext;
import com.silentgo.core.SilentGoKit;
import com.silentgo.kit.StringKit;
import com.silentgo.logger.Logger;
import com.silentgo.logger.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * Created by teddyzhu on 16/7/15.
 */
public class SilentGoServletLoader extends HttpServlet {

    private static Logger LOGGER = LoggerFactory.getLogger(SilentGoServletLoader.class);

    private ServletContext servletContext;

    private SilentGoConfig config;

    private static SilentGo appContext = SilentGo.getContext();

    public SilentGoServletLoader() {
    }


    @Override
    public void init(ServletConfig config) throws ServletException {

        servletContext = config.getServletContext();
        if (!appContext.isLoaded()) {

            LOGGER.info("Start SilentGo Loader ...");

            LOGGER.info("file.encoding = {}", System.getProperty("file.encoding"));

            long startTime = System.currentTimeMillis();

            SilentGoContext.setContext(servletContext);

            this.config = appContext.getConfig();
            if (null == config) {
                String configClassName = config.getInitParameter("config");
                if (!StringKit.isNullOrEmpty(configClassName)) {
                    this.config = getConfig(configClassName);
                } else {
                    this.config = new SilentGoConfig() {
                    };
                }
            }



            LOGGER.info("SilentGo Loader initialize successfully, Time : {} ms.", System.currentTimeMillis() - startTime);
        }
    }

    @Override
    public void destroy() {
        super.destroy();

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
            if (!StringKit.isNullOrEmpty(className)) {
                Class<SilentGoConfig> applicationClass = (Class<SilentGoConfig>) Class.forName(className);

                configClass = applicationClass == null ? configClass : applicationClass.newInstance();

            } else {
                LOGGER.error("Config Class is not found!");
                throw new ServletException("Config Class is not found!");
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
        return configClass;
    }

}
