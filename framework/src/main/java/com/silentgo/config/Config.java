package com.silentgo.config;

/**
 * Project : silentgo
 * com.silentgo.config
 *
 * @author    <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by  on 16/7/18.
 */
public interface Config {

    default void init(SilentGoConfig config) {
    }

    default void afterInit(SilentGoConfig config) {
    }
}
