package com.silentgo.config;

import com.silentgo.core.SilentGoKit;

/**
 * Project : silentgo
 * com.silentgo.config
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by  on 16/7/15.
 */
public interface SilentGoConfig {

    default void init(SilentGoKit kit) {
    }

    default void afterApplicationStart(SilentGoKit kit) {
    }
}
