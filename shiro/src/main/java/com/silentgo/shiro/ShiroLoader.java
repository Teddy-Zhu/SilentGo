package com.silentgo.shiro;

import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;
import org.apache.shiro.config.ConfigurationException;
import org.apache.shiro.config.ResourceConfigurable;
import org.apache.shiro.util.ClassUtils;
import org.apache.shiro.util.LifecycleUtils;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.util.UnknownClassException;
import org.apache.shiro.web.env.EnvironmentLoader;
import org.apache.shiro.web.env.IniWebEnvironment;
import org.apache.shiro.web.env.MutableWebEnvironment;
import org.apache.shiro.web.env.WebEnvironment;
import javax.servlet.ServletContext;

/**
 * Created by teddy on 2015/8/8.
 */
public class ShiroLoader {


    public static final String ENVIRONMENT_CLASS_PARAM = "shiroEnvironmentClass";


    public static final String CONFIG_LOCATIONS_PARAM = "shiroConfigLocations";

    public static final String ENVIRONMENT_ATTRIBUTE_KEY = EnvironmentLoader.class.getName() + ".ENVIRONMENT_ATTRIBUTE_KEY";


    private static final Log LOGGER = LogFactory.get();

    public WebEnvironment initEnvironment(ServletContext servletContext) throws IllegalStateException {

        if (servletContext.getAttribute(ENVIRONMENT_ATTRIBUTE_KEY) != null) {
            String msg = "There is already a Shiro environment associated with the current ServletContext.  " +
                    "Check if you have multiple EnvironmentLoader* definitions in your web.xml!";
            throw new IllegalStateException(msg);
        }

        servletContext.log("Initializing Shiro environment");
        LOGGER.info("Starting Shiro environment initialization.");

        long startTime = System.currentTimeMillis();

        try {
            WebEnvironment environment = createEnvironment(servletContext);
            servletContext.setAttribute(ENVIRONMENT_ATTRIBUTE_KEY, environment);

            LOGGER.debug("Published WebEnvironment as ServletContext attribute with name [{}]",
                    ENVIRONMENT_ATTRIBUTE_KEY);

            if (LOGGER.isInfoEnabled()) {
                long elapsed = System.currentTimeMillis() - startTime;
                LOGGER.info("Shiro environment initialized in {} ms.", elapsed);
            }

            return environment;
        } catch (RuntimeException ex) {
            LOGGER.error("Shiro environment initialization failed", ex);
            servletContext.setAttribute(ENVIRONMENT_ATTRIBUTE_KEY, ex);
            throw ex;
        } catch (Error err) {
            LOGGER.error("Shiro environment initialization failed", err);
            servletContext.setAttribute(ENVIRONMENT_ATTRIBUTE_KEY, err);
            throw err;
        }
    }


    protected Class<?> determineWebEnvironmentClass(ServletContext servletContext) {
        String className = servletContext.getInitParameter(ENVIRONMENT_CLASS_PARAM);
        if (className != null) {
            try {
                return ClassUtils.forName(className);
            } catch (UnknownClassException ex) {
                throw new ConfigurationException(
                        "Failed to load custom WebEnvironment class [" + className + "]", ex);
            }
        } else {
            return IniWebEnvironment.class;
        }
    }

    protected WebEnvironment createEnvironment(ServletContext sc) {

        Class<?> clazz = determineWebEnvironmentClass(sc);
        if (!MutableWebEnvironment.class.isAssignableFrom(clazz)) {
            throw new ConfigurationException("Custom WebEnvironment class [" + clazz.getName() +
                    "] is not of required type [" + WebEnvironment.class.getName() + "]");
        }

        String configLocations = sc.getInitParameter(CONFIG_LOCATIONS_PARAM);
        boolean configSpecified = StringUtils.hasText(configLocations);

        if (configSpecified && !(ResourceConfigurable.class.isAssignableFrom(clazz))) {
            String msg = "WebEnvironment class [" + clazz.getName() + "] does not implement the " +
                    ResourceConfigurable.class.getName() + "interface.  This is required to accept any " +
                    "configured " + CONFIG_LOCATIONS_PARAM + "value(s).";
            throw new ConfigurationException(msg);
        }

        MutableWebEnvironment environment = (MutableWebEnvironment) ClassUtils.newInstance(clazz);

        environment.setServletContext(sc);

        if (configSpecified && (environment instanceof ResourceConfigurable)) {
            ((ResourceConfigurable) environment).setConfigLocations(configLocations);
        }

        customizeEnvironment(environment);

        //environment.setWebSecurityManager(defaultWebSecurityManager);


        //LifecycleUtils.initialBuild(environment);

        return environment;
    }

    protected void customizeEnvironment(WebEnvironment environment) {
    }

    public void destroyEnvironment(ServletContext servletContext) {
        servletContext.log("Cleaning up Shiro Environment");
        try {
            Object environment = servletContext.getAttribute(ENVIRONMENT_ATTRIBUTE_KEY);
            LifecycleUtils.destroy(environment);
        } finally {
            servletContext.removeAttribute(ENVIRONMENT_ATTRIBUTE_KEY);
        }
    }
}
