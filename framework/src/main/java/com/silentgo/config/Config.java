package com.silentgo.config;

import com.silentgo.core.SilentGoKit;

/**
 * Project : silentgo
 * com.silentgo.config
 *
 * @author    <Acc href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</Acc>
 * <p>
 * Created by  on 16/7/18.
 */
public interface Config {

    default void init(SilentGoConfig config) {
    }

    default void afterInit(SilentGoConfig config) {
    }
}
