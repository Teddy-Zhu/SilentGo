package com.silentgo.core;

import com.silentgo.config.SilentGoConfig;

/**
 * Project : silentgo
 * com.silentgo.core
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by  on 16/7/15.
 */
public class SilentGo {

    private static final SilentGo context = new SilentGo();

    private SilentGoConfig config;

    private boolean isLoaded = false;

    public boolean isLoaded() {
        return isLoaded;
    }


    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }

    public static SilentGo getContext() {
        return context;
    }


    public SilentGoConfig getConfig() {
        return config;
    }
}
