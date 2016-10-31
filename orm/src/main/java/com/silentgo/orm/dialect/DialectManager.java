package com.silentgo.orm.dialect;

import com.silentgo.orm.base.BaseDaoDialect;
import com.silentgo.orm.base.DBType;

import java.util.HashMap;
import java.util.Map;

/**
 * Project : SilentGo
 * Package : com.silentgo.orm.dialect
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/29.
 */
public class DialectManager {

    private static Map<DBType, BaseDaoDialect> dialectMap;

    static {
        dialectMap = new HashMap<>();
        dialectMap.put(DBType.MYSQL, new MysqlBaseDaoDialect());
    }

    public static void add(DBType type, BaseDaoDialect daoDialect) {
        dialectMap.put(type, daoDialect);
    }

    public static BaseDaoDialect getDialect(DBType type) {
        return dialectMap.get(type);
    }
}
