package com.silentgo.core.db;

import com.silentgo.core.SilentGo;
import com.silentgo.core.build.Factory;
import com.silentgo.core.exception.AppBuildException;
import com.silentgo.core.exception.AppReleaseException;
import com.silentgo.core.plugin.db.bridge.mysql.MysqlBaseDaoDialect;
import com.silentgo.core.support.BaseFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Project : parent
 * Package : com.silentgo.core.plugin.db
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/28.
 */
@Factory
public class DialectFactory extends BaseFactory {

    private Map<DBType, BaseDaoDialect> dialectMap;

    public BaseDaoDialect getDialect(DBType type) {
        return dialectMap.get(type);
    }

    @Override
    public boolean initialize(SilentGo me) throws AppBuildException {
        dialectMap = new HashMap<>();
        dialectMap.put(DBType.MYSQL, new MysqlBaseDaoDialect());

        return true;
    }

    @Override
    public boolean destroy(SilentGo me) throws AppReleaseException {
        dialectMap.clear();
        return true;
    }
}
