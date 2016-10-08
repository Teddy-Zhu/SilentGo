package com.silentgo.core.db;

import com.silentgo.core.SilentGo;
import com.silentgo.core.build.Factory;
import com.silentgo.core.config.SilentGoConfig;
import com.silentgo.core.exception.AppReleaseException;
import com.silentgo.core.support.BaseFactory;

/**
 * Project : silentgo
 * com.silentgo.core.plugin.db.bridge
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/23.
 */
@Factory
public class SqlFactory extends BaseFactory {

    @Override
    public boolean initialize(SilentGo me) {
        SilentGoConfig config = me.getConfig();

        if (config.getDbType() == null) return true;
        if (config.getUserProp() != null) {
            config.addAbstractConfig(new DBConfig(config.getDbType(), config.getUserProp()));
        } else {
            config.addAbstractConfig(new DBConfig(config.getDbType(), "config.properties"));
        }

        return true;
    }

    @Override
    public boolean destroy(SilentGo me) throws AppReleaseException {
        return true;
    }


}
