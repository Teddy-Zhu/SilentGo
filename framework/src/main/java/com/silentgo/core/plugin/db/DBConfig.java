package com.silentgo.core.plugin.db;

import com.silentgo.core.config.AbstractConfig;
import com.silentgo.orm.base.DBManager;
import com.silentgo.orm.jdbc.JDBCManager;
import com.silentgo.orm.kit.configKit;

/**
 * Project : silentgo
 * com.silentgo.core.plugin.db
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/22.
 */
public class DBConfig extends AbstractConfig {
    private static String name = "DataBase";

    private DBManager manager;

    public DBConfig(String dbType, String fileName) {
        if (DBType.MYSQL.equals(dbType.toLowerCase())) {
            manager = JDBCManager.getInstance();
            com.silentgo.orm.base.DBConfig config = configKit.getConfig("config.properties");
            manager.initial(config);
        }
    }

    @Override
    public String name() {
        return name;
    }
}
