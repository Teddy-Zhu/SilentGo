package com.silentgo.core;

import com.silentgo.config.SilentGoConfig;
import com.silentgo.core.action.ActionChain;
import com.silentgo.core.ioc.bean.BeanDefinition;
import com.silentgo.core.ioc.bean.BeanFactory;
import com.silentgo.core.ioc.bean.SilentGoBean;
import com.silentgo.core.render.RenderFactory;
import com.silentgo.core.render.SilentGoRender;
import com.silentgo.core.route.RouteFactory;
import com.silentgo.core.support.AnnotationManager;

import javax.servlet.ServletContext;
import java.util.List;

/**
 * Project : silentgo
 * com.silentgo.core
 *
 * @author <Acc href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</Acc>
 *         <p>
 *         Created by  on 16/7/15.
 */
public class SilentGo {

    private static class SilentGoHolder {
        static SilentGo instance = new SilentGo();
    }

    public static SilentGo getInstance() {
        return SilentGoHolder.instance;
    }

    private AnnotationManager annotationManager;

    private SilentGoConfig config;

    private ServletContext Context;

    private boolean isLoaded = false;

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }

    public SilentGoConfig getConfig() {
        return config;
    }

    public boolean isDevMode() {
        return config.isDevMode();
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setConfig(SilentGoConfig config) {
        this.config = config;
    }

    public void setContext(ServletContext context) {
        Context = context;
    }

    public void setAnnotationManager(AnnotationManager annotationManager) {
        this.annotationManager = annotationManager;
    }

    public AnnotationManager getAnnotationManager() {
        return annotationManager;
    }

}
