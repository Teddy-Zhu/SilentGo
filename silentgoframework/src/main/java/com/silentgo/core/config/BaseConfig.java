package com.silentgo.core.config;

import com.silentgo.core.aop.annotationintercept.support.AnnotationInterceptor;
import com.silentgo.core.aop.validator.support.ValidatorInterceptor;
import com.silentgo.core.build.SilentGoBuilder;
import com.silentgo.core.build.SilentGoReleaser;
import com.silentgo.core.route.RoutePaser;
import com.silentgo.core.route.support.routeparse.DefaultRouteParser;
import com.silentgo.kit.ClassKit;
import com.silentgo.kit.json.FastJsonPaser;
import com.silentgo.kit.json.JsonPaser;

import java.util.ArrayList;
import java.util.List;

/**
 * Project : silentgo
 * com.silentgo.core.config
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/2.
 */
public class BaseConfig extends InterConfig {

    private JsonPaser jsonPaser = new FastJsonPaser();

    private RoutePaser routePaser = new DefaultRouteParser();

    private String BaseView = Const.BaseView;

    private final List<SilentGoBuilder> builders = new ArrayList<>();

    private final List<SilentGoReleaser> releasers = new ArrayList<>();

    private List<String> staticFolder = new ArrayList<>();

    private List<String> scanPackages = new ArrayList<>();

    private List<String> scanJars = new ArrayList<>();

    private FileUploadConfig fileUploadConfig = new FileUploadConfig(ClassKit.getWebRootPath() + "/UploadFile", -1, 10240, true);

    private boolean devMode = false;

    private String encoding = "utf-8";

    private int contextPathLength;

    @SuppressWarnings("unchecked")
    private ArrayList interceptors = new ArrayList() {{
        add(new AnnotationInterceptor());
        add(new ValidatorInterceptor());
    }};

    public JsonPaser getJsonPaser() {
        return jsonPaser;
    }

    public void setJsonPaser(JsonPaser jsonPaser) {
        this.jsonPaser = jsonPaser;
    }

    public RoutePaser getRoutePaser() {
        return routePaser;
    }

    public void setRoutePaser(RoutePaser routePaser) {
        this.routePaser = routePaser;
    }

    public String getBaseView() {
        return BaseView;
    }

    public void setBaseView(String baseView) {
        BaseView = baseView;
    }

    public List<SilentGoBuilder> getBuilders() {
        return builders;
    }

    public List<SilentGoReleaser> getReleasers() {
        return releasers;
    }

    public List<String> getStaticFolder() {
        return staticFolder;
    }

    public void setStaticFolder(List<String> staticFolder) {
        this.staticFolder = staticFolder;
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

    public FileUploadConfig getFileUploadConfig() {
        return fileUploadConfig;
    }

    public void setFileUploadConfig(FileUploadConfig fileUploadConfig) {
        this.fileUploadConfig = fileUploadConfig;
    }

    public boolean isDevMode() {
        return devMode;
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public int getContextPathLength() {
        return contextPathLength;
    }

    public void setContextPathLength(int contextPathLength) {
        this.contextPathLength = contextPathLength;
    }

    public ArrayList getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(ArrayList interceptors) {
        this.interceptors = interceptors;
    }
}
