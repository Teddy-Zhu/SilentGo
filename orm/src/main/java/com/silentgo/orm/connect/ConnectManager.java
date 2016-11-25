package com.silentgo.orm.connect;

import com.silentgo.orm.base.DBConfig;
import com.silentgo.orm.base.DBConnect;
import com.silentgo.orm.base.DBManager;
import com.silentgo.orm.base.DBType;
import com.silentgo.orm.infobuilder.BaseTableBuilder;
import com.silentgo.orm.jdbc.JDBCManager;
import com.silentgo.orm.kit.configKit;
import com.silentgo.utils.PropKit;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Project : SilentGo
 * Package : com.silentgo.orm.connect
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/29.
 */
public class ConnectManager {

    private Map<DBType, DBManager> dbManagerMap = new ConcurrentHashMap<>();

    private static class ConnectManagerHolder {
        static ConnectManager instance = new ConnectManager();
    }

    public static ConnectManager me() {
        return ConnectManagerHolder.instance;
    }

    public void configManager(String dbType, String name, PropKit propKit) {
        if (DBType.MYSQL.equals(dbType.toLowerCase())) {
            DBManager manager = new JDBCManager();
            DBConfig config = configKit.getConfig(propKit);
            config.setName(name);
            manager.initial(config);
            dbManagerMap.put(DBType.MYSQL, manager);
        }
    }

    public void configManager(String dbType, String name, String fileName) {
        if (DBType.MYSQL.equals(dbType.toLowerCase())) {
            DBManager manager = new JDBCManager();
            DBConfig config = configKit.getConfig(fileName);
            config.setName(name);
            manager.initial(config);
            dbManagerMap.put(DBType.MYSQL, manager);
        }
    }

    public void releaseConnect(DBType type, String name) {
        dbManagerMap.get(type).releaseConnect(name);
    }

    public DBConnect getConnect(DBType type, String name) {
        return dbManagerMap.get(type).getConnect(name);
    }

    public DBConnect getNewConnect(DBType type, String name) {
        return dbManagerMap.get(type).getUnSafeConnect(name);
    }

    public boolean releaseNewConnect(DBType type, String name, DBConnect connect) {
        return dbManagerMap.get(type).releaseUnSafeConnect(name, connect);
    }

    public DBConnect getThreadConnect(DBType type, String name) {
        return dbManagerMap.get(type).getThreadConnect(name);
    }

    public boolean setTheadConnect(DBType type, String name, DBConnect connect) {
        return dbManagerMap.get(type).setThreadConnect(name, connect);
    }

}
