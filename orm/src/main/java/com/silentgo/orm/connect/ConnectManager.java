package com.silentgo.orm.connect;

import com.silentgo.orm.base.DBConfig;
import com.silentgo.orm.base.DBConnect;
import com.silentgo.orm.base.DBManager;
import com.silentgo.orm.base.DBType;
import com.silentgo.orm.infobuilder.BaseTableBuilder;
import com.silentgo.orm.jdbc.JDBCManager;
import com.silentgo.orm.kit.configKit;
import com.silentgo.utils.PropKit;

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
    //CONNECT INHERIT
    private ThreadLocal<DBConnect> threadConnect = new InheritableThreadLocal<>();
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

    public void releaseConnect() {
        DBConnect connect = threadConnect.get();
        if (connect != null) {
            threadConnect.remove();
            connect.release();
        }
    }

    public DBConnect getConnect(DBType type, String name) {
        DBConnect connect = threadConnect.get();
        if (connect == null) {
            connect = dbManagerMap.get(type).getConnect(name);
            threadConnect.set(connect);
        }
        return connect;
    }

}
