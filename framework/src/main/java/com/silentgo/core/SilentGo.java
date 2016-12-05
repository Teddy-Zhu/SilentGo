package com.silentgo.core;

import com.silentgo.core.config.SilentGoConfig;
import com.silentgo.core.support.AnnotationManager;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.orm.base.DBConnect;
import com.silentgo.utils.json.JsonPaser;

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

    public static SilentGo me() {
        return SilentGoHolder.instance;
    }

    private AnnotationManager annotationManager;

    private SilentGoConfig config;

    private ServletContext Context;

    public SilentGoConfig getConfig() {
        return config;
    }

    public DBConnect getConnect(String name) {
        return config.getConnect(name);
    }

    public void releaseConnect(DBConnect connect) {
        config.releaseConnect(config.getDbType().toLowerCase(),connect);
    }

    public DBConnect getConnect() {
        return getConnect(config.getDbType());
    }

    public boolean hasConnecct(String name) {
        return config.hasConnect(name);
    }

    public boolean hasConnecct() {
        return hasConnecct(config.getDbType());
    }

    public boolean isDevMode() {
        return config.isDevMode();
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
        return config.getFactory(t, me());
    }

    public JsonPaser json() {
        return config.getJsonPaser();
    }
}
