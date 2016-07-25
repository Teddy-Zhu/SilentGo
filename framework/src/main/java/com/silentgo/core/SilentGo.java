package com.silentgo.core;

import com.silentgo.config.SilentGoConfig;
import com.silentgo.core.action.ActionChain;
import com.silentgo.core.ioc.bean.BeanDefinition;
import com.silentgo.core.ioc.bean.SilentGoBean;
import com.silentgo.core.render.RenderFactory;
import com.silentgo.core.render.SilentGoRender;
import com.silentgo.core.route.RouteFactory;

import javax.servlet.ServletContext;
import java.util.List;

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

    private SilentGoConfig config;

    private ActionChain actionChain;

    private RouteFactory routeFactory;

    private SilentGoBean beanFactory;

    private RenderFactory renderFactory;


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

    public ActionChain getActionChain() {
        return actionChain;
    }

    public void setActionChain(ActionChain actionChain) {
        this.actionChain = actionChain;
    }

    public void setContext(ServletContext context) {
        Context = context;
    }


    public void initBeanFactory(List<BeanDefinition> beanDefinitions) {
        this.beanFactory = new SilentGoBean(beanDefinitions, config);
    }

    public void initRenderFactory(SilentGoRender render) {
        this.renderFactory = new RenderFactory(render , config);
    }

}
