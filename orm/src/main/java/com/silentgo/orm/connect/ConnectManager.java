package com.silentgo.orm.connect;

import com.silentgo.orm.base.DBConfig;
import com.silentgo.orm.base.DBConnect;
import com.silentgo.orm.base.DBManager;
import com.silentgo.orm.base.DBType;
import com.silentgo.orm.kit.configKit;
import com.silentgo.utils.Assert;
import com.silentgo.utils.PropKit;
import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;

import java.util.HashMap;
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

    private static final Log LOGGER = LogFactory.get();
    private static Map<DBType, Class<? extends DBManager>> classMap = new HashMap<>();
    private Map<DBType, DBManager> dbManagerMap = new ConcurrentHashMap<>();

    private static class ConnectManagerHolder {
        static ConnectManager instance = new ConnectManager();
    }

    public static ConnectManager me() {
        return ConnectManagerHolder.instance;
    }

    public void configManager(String dbType, String name, DBConfig config) {
        Assert.isNotBlank(dbType, "db type can not be empty");
        Assert.isNotNull(config, "config can not be null");

        DBType type = DBType.parse(dbType);

        if (type == null) {
            LOGGER.error("init connect error , {} type can not found!", dbType);
            return;
        }

        DBManager manager = null;
        try {
            manager = classMap.get(type).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error(e, "init connect , create manger error");
        }
        config.setName(name);
        manager.initial(config);
        dbManagerMap.put(type, manager);
    }

    public void configManager(String dbType, String name, PropKit propKit) {
        DBConfig config = configKit.getConfig(propKit);
        this.configManager(dbType, name, config);
    }

    public void configManager(String dbType, String name, String fileName) {
        DBConfig config = configKit.getConfig(fileName);
        this.configManager(dbType, name, config);
    }

    public void releaseConnect(DBType type, String name, DBConnect connect) {
        dbManagerMap.get(type).releaseConnect(name, connect);
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

    public boolean setThreadConnect(DBType type, String name, DBConnect connect) {
        return dbManagerMap.get(type).setThreadConnect(name, connect);
    }

    public static void setDBManager(DBType type, Class<? extends DBManager> clz) {
        classMap.put(type, clz);
    }

}
