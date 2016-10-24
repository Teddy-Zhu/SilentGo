package com.silentgo.core.db;

import com.silentgo.core.config.AbstractConfig;
import com.silentgo.orm.base.DBManager;
import com.silentgo.orm.base.DBType;
import com.silentgo.orm.jdbc.JDBCManager;
import com.silentgo.orm.kit.configKit;
import com.silentgo.utils.PropKit;

/**
 * Project : silentgo
 * com.silentgo.core.plugin.db
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/22.
 */
public class DBConfig extends AbstractConfig {
    private String name = "mysql";

    private DBManager manager;

    public DBConfig(String dbType, PropKit propKit) {
        if (DBType.MYSQL.equals(dbType.toLowerCase())) {
            manager = JDBCManager.getInstance();
            com.silentgo.orm.base.DBConfig config = configKit.getConfig(propKit);
            config.setName(name);
            manager.initial(config);
        }
    }

    public DBConfig(String dbType, String fileName) {
        if (DBType.MYSQL.equals(dbType.toLowerCase())) {
            manager = JDBCManager.getInstance();
            com.silentgo.orm.base.DBConfig config = configKit.getConfig("config.properties");
            config.setName(name);
            manager.initial(config);
        }
    }

    public DBManager getManager() {
        return manager;
    }

    public void setManager(DBManager manager) {
        this.manager = manager;
    }

    @Override
    public String name() {
        return name;
    }
}
