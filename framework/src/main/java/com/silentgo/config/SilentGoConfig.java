package com.silentgo.config;

import com.silentgo.build.Builder;
import com.silentgo.core.action.ActionChain;
import com.silentgo.core.aop.Interceptor;
import com.silentgo.core.aop.annotationintercept.AnnotationInterceptor;
import com.silentgo.core.aop.validator.support.ValidatorInterceptor;
import com.silentgo.core.ioc.bean.BeanFactory;
import com.silentgo.core.ioc.bean.SilentGoBean;
import com.silentgo.core.render.RenderFactory;
import com.silentgo.core.route.support.RouteFactory;
import com.silentgo.kit.CollectionKit;

import java.util.ArrayList;
import java.util.List;

/**
 * Project : silentgo
 * com.silentgo.config
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/15.
 */
public class SilentGoConfig {

    private ActionChain actionChain;

    private RouteFactory routeFactory;

    private RenderFactory renderFactory;

    private BeanFactory beanFactory = new SilentGoBean();

    public List<Builder> builders = new ArrayList<>();

    public List<String> scanPackages;

    public List<String> scanJars;

    public boolean devMode = false;

    public String encoding = "utf-8";

    public int contextPathLength;

    private final ArrayList interceptors = new ArrayList() {{
        add(new AnnotationInterceptor());
        add(new ValidatorInterceptor());
    }};

    public List<Builder> getBuilders() {
        return builders;
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }


    public boolean isDevMode() {
        return devMode;
    }

    public int getContextPathLength() {
        return contextPathLength;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void setContextPathLength(int contextPathLength) {
        this.contextPathLength = contextPathLength;
    }

    public SilentGoConfig(List<String> scanPackages, List<String> scanJars, boolean devMode, String encoding, int contextPathLength) {
        this.scanPackages = scanPackages;
        this.scanJars = scanJars;
        this.devMode = devMode;
        this.encoding = encoding;
        this.contextPathLength = contextPathLength;
    }

    public List<String> getScanPackages() {
        return scanPackages;
    }

    public void setScanPackages(List<String> scanPackages) {
        this.scanPackages = scanPackages;
    }

    public List<String> getScanJars() {
        return scanJars;
    }

    public void setScanJars(List<String> scanJars) {
        this.scanJars = scanJars;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public ActionChain getActionChain() {
        return actionChain;
    }

    public void setActionChain(ActionChain actionChain) {
        this.actionChain = actionChain;
    }

    public RouteFactory getRouteFactory() {
        return routeFactory;
    }

    public void setRouteFactory(RouteFactory routeFactory) {
        this.routeFactory = routeFactory;
    }

    public RenderFactory getRenderFactory() {
        return renderFactory;
    }

    public void setRenderFactory(RenderFactory renderFactory) {
        this.renderFactory = renderFactory;
    }

    public boolean addInterceptor(Interceptor interceptor) {
        return CollectionKit.ListAdd(interceptors, interceptor, false);
    }

    public ArrayList getInterceptors() {
        return interceptors;
    }

}
