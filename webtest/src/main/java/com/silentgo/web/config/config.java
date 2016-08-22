package com.silentgo.web.config;

import com.silentgo.core.config.Config;
import com.silentgo.core.config.SilentGoConfig;

/**
 * Project : silentgo
 * com.silentgo.web.config
 *
 * @author    <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by teddyzhu on 16/8/19.
 */
public class config implements Config {

    @Override
    public void init(SilentGoConfig config) {
        config.addStatic("/favicon.ico");
    }
}
