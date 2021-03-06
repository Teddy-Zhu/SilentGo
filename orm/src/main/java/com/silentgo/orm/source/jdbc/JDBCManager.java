package com.silentgo.orm.source.jdbc;

import com.silentgo.orm.base.*;
import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Project : silentgo
 * com.silentgo.orm.source.jdbc
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/19.
 */
public class JDBCManager implements DBManager {

    private static final Log LOGGER = LogFactory.get();
    public Map<String, JDBCPool> poolHashMap;

    @Override
    public void initial(DBConfig... configs) {
        poolHashMap = new ConcurrentHashMap<>();
        for (DBConfig config : configs) {
            JDBCDataSource source = new JDBCDataSource(config.getName(), config);
            poolHashMap.put(source.getName(), new JDBCPool(source));
        }
    }

    @Override
    public DBPool getPool(String name) {
        if (!poolHashMap.containsKey(name)) {
            LOGGER.info("can not find the database pool:{}", name);
            throw new RuntimeException("find jdbc pool error");
        }
        return poolHashMap.get(name);
    }

    @Override
    public DBConnect getConnect(String name) {
        return getPool(name).getDBConnect();
    }

    @Override
    public DBConnect getUnSafeConnect(String name) {
        return getPool(name).getUnSafeDBConnect();
    }

    @Override
    public boolean releaseUnSafeConnect(String name, DBConnect connect) {
        return getPool(name).releaseUnSafeDBConnect(connect);
    }

    @Override
    public boolean releaseConnect(String name, DBConnect connect) {
        return getPool(name).releaseDBConnect(connect);
    }

    @Override
    public DBConnect getThreadConnect(String name) {
        return getPool(name).getThreadConnect();
    }

    @Override
    public boolean setThreadConnect(String name, DBConnect connect) {
        return getPool(name).setThreadConnect(connect);
    }

    @Override
    public boolean destory() {
        poolHashMap.forEach((k, v) -> v.destory());
        return true;
    }
}
