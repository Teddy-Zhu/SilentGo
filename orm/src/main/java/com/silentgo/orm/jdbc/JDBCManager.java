package com.silentgo.orm.jdbc;

import com.silentgo.orm.*;
import com.silentgo.utils.PropKit;
import com.silentgo.utils.random.RandomUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.orm.jdbc
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/19.
 */
public class JDBCManager implements DBManager {

    public static Map<String, DBPool> poolHashMap;

    //singleton
    private static class JDBCManagerHolder {
        static JDBCManager instance = new JDBCManager();
    }

    public static JDBCManager getInstance() {
        return JDBCManagerHolder.instance;
    }

    public void init(DBConfig... configs) {
        poolHashMap = new HashMap<>();
        for (DBConfig config : configs) {
            DataSource source = new DataSource(RandomUtil.String(20), config);
            poolHashMap.put(source.getName(), new JDBCPool(source));
        }
    }

    public void init(PropKit propKit) {

    }

    @Override
    public DBPool getPool(String name) {
        return null;
    }

    @Override
    public DBConnect getConnect() {
        return null;
    }
}
