package com.silentgo.config;

import com.silentgo.build.SilentGoBuilder;
import com.silentgo.core.action.ActionChain;
import com.silentgo.core.aop.Interceptor;
import com.silentgo.core.aop.annotationintercept.support.AnnotationInterceptor;
import com.silentgo.core.aop.validator.support.ValidatorInterceptor;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.kit.CollectionKit;
import com.silentgo.kit.PropKit;
import com.silentgo.kit.SilentGoContext;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private Map<String, BaseFactory> factoryMap = new HashMap<>();

    private ThreadLocal<SilentGoContext> ctx = new ThreadLocal<>();

    private List<SilentGoBuilder> builders = new ArrayList<>();

    private List<String> staticFolder = new ArrayList<>();

    private List<String> scanPackages;

    private List<String> scanJars;

    private PropKit propKit;

    private boolean devMode = false;

    private String encoding = "utf-8";

    private int contextPathLength;

    @SuppressWarnings("unchecked")
    private ArrayList interceptors = new ArrayList() {{
        add(new AnnotationInterceptor());
        add(new ValidatorInterceptor());
    }};

    public List<SilentGoBuilder> getBuilders() {
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

    public SilentGoConfig(List<String> scanPackages, List<String> scanJars, boolean devMode, String encoding, int contextPathLength, String fileName) {
        this.scanPackages = scanPackages;
        this.scanJars = scanJars;
        this.devMode = devMode;
        this.encoding = encoding;
        this.contextPathLength = contextPathLength;
        this.propKit = new PropKit(fileName, encoding);
        this.scanJars.add(Const.ApplicationName + "-" + propKit.getValue(Const.Version) + ".jar");
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

    public ActionChain getActionChain() {
        return actionChain;
    }

    public void setActionChain(ActionChain actionChain) {
        this.actionChain = actionChain;
    }

    @SuppressWarnings("unchecked")
    public boolean addInterceptor(Interceptor interceptor) {
        return CollectionKit.ListAdd(interceptors, interceptor, false);
    }

    public ArrayList getInterceptors() {
        return interceptors;
    }


    public boolean addFactory(BaseFactory baseFactory) {
        return CollectionKit.MapAdd(factoryMap, baseFactory.getName(), baseFactory);
    }

    public boolean addFactory(Class<? extends BaseFactory> factoryClz) {
        try {
            return addFactory(factoryClz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    public BaseFactory getFactory(String name) {
        return factoryMap.get(name);
    }

    public List<String> getStaticFolder() {
        return staticFolder;
    }

    public boolean addStatic(String name) {
        return CollectionKit.ListAdd(staticFolder, name);
    }

    public boolean setCtx(Request request, Response response) {
        ctx.set(new SilentGoContext(response, request));
        return true;
    }

    public ThreadLocal<SilentGoContext> getCtx() {
        return ctx;
    }
}
