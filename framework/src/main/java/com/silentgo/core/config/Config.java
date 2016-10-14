package com.silentgo.core.config;

/**
 * Project : silentgo
 * com.silentgo.core.config
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by  on 16/7/18.
 */
public interface Config {

    default void initialBuild(SilentGoConfig config) {
    }

    default void afterInit(SilentGoConfig config) {
    }
}
