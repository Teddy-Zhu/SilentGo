package com.silentgo.config;

import com.silentgo.core.aop.IInterceptor;

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

    private Map<String, List<IInterceptor>> methodOrClassInterceptorMap;

    private List<String> scanPackages;

    private List<String> scanJars;

    private boolean devMode;

    private String encoding = "utf-8";

    private int contextPathLength;

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

}
