package com.silentgo.core.plugin.db;

import com.silentgo.core.SilentGo;
import com.silentgo.core.build.SilentGoBuilder;
import com.silentgo.core.exception.AppBuildException;

/**
 * Project : silentgo
 * com.silentgo.core.plugin.db
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/22.
 */
public class DBbuilder extends SilentGoBuilder {
    @Override
    public boolean build(SilentGo me) throws AppBuildException {

        me.getConfig().addAbstractConfig(new DBConfig(me.getConfig().getDbType(), "application.properties"));
        return false;
    }
}
