package com.silentgo.test.config;

import com.silentgo.core.config.SilentGoConfig;
import com.silentgo.orm.base.DBType;
import com.silentgo.utils.PropKit;

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
    public void initialBuild(SilentGoConfig config) {

        config.setDbType(DBType.MYSQL.getName());

        config.setUserProp(new PropKit("app.properties"));
        config.addEndStatic(".ico");
        config.addEndStatic(".js");
        config.addStaticMapping("/js/", "/");
    }

    @Override
    public void afterInit(SilentGoConfig config) {

    }
}
