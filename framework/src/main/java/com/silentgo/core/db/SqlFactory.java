package com.silentgo.core.db;

import com.silentgo.core.SilentGo;
import com.silentgo.core.build.Factory;
import com.silentgo.core.config.SilentGoConfig;
import com.silentgo.core.exception.AppReleaseException;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.orm.base.DBConfig;
import com.silentgo.orm.connect.ConnectManager;
import com.silentgo.orm.infobuilder.BaseTableBuilder;
import com.silentgo.utils.CollectionKit;

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

        if (config.getDbType() == null) return false;
        if (CollectionKit.isEmpty(config.getDbConfigList())) {
            if (config.getUserProp() != null) {
                ConnectManager.me().configManager(config.getDbType(), config.getDbType(), config.getUserProp());
            } else {
                ConnectManager.me().configManager(config.getDbType(), config.getDbType(), "config.properties");
            }
        } else {
            for (DBConfig dbConfig : config.getDbConfigList()) {
                ConnectManager.me().configManager(config.getDbType(), config.getDbType(), dbConfig);
            }
        }


        return true;
    }

    @Override
    public boolean destroy(SilentGo me) throws AppReleaseException {
        return true;
    }


}
