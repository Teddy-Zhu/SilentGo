package com.silentgo.orm.jdbc;

import com.silentgo.orm.base.*;
import com.silentgo.utils.logger.Logger;
import com.silentgo.utils.logger.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Project : silentgo
 * com.silentgo.orm.jdbc
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/19.
 */
public class JDBCManager implements DBManager {

    private static final Logger LOGGER = LoggerFactory.getLog(JDBCManager.class);

    public static Map<String, DBPool> poolHashMap;

    //singleton
    private static class JDBCManagerHolder {
        static JDBCManager instance = new JDBCManager();
    }

    public static JDBCManager getInstance() {
        return JDBCManagerHolder.instance;
    }

    @Override
    public void initial(DBConfig... configs) {
        poolHashMap = new ConcurrentHashMap<>();
        for (DBConfig config : configs) {
            DataSource source = new DataSource(config.getName(), config);
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
    public boolean destory() {
        poolHashMap.forEach((k,v)-> v.destory());
        return true;
    }
}
