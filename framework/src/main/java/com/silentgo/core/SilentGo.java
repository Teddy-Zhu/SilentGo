package com.silentgo.core;

import com.silentgo.core.config.SilentGoConfig;
import com.silentgo.core.support.AnnotationManager;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.orm.base.DBConnect;

import javax.servlet.ServletContext;

/**
 * Project : silentgo
 * com.silentgo.core
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
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

    public DBConnect getConnect(String name) {
        return config.getConnect(config.getDbType(), name);
    }

    public void releaseConnect(String name) {
        config.releaseConnect(config.getDbType(), name);
    }

    public void releaseConnect() {
        releaseConnect("mysql");
    }

    public DBConnect getConnect() {
        return getConnect("mysql");
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

    public ServletContext getContext() {
        return Context;
    }

    public void setAnnotationManager(AnnotationManager annotationManager) {
        this.annotationManager = annotationManager;
    }

    public AnnotationManager getAnnotationManager() {
        return annotationManager;
    }

    public <T extends BaseFactory> T getFactory(Class<T> t) {
        return config.getFactory(t, getInstance());
    }

}
