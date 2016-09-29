package com.silentgo.test.config;

import com.silentgo.core.config.SilentGoConfig;
import com.silentgo.core.db.DBType;

/**
 * Project : parent
 * Package : com.silentgo.test.config
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/27.
 */
public class Config implements com.silentgo.core.config.Config {
    @Override
    public void init(SilentGoConfig config) {

        config.setDbType(DBType.MYSQL.getName());

        config.setPropfile("app.properties");

    }

    @Override
    public void afterInit(SilentGoConfig config) {

    }
}
