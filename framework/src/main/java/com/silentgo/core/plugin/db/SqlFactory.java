package com.silentgo.core.plugin.db;

import com.silentgo.core.SilentGo;
import com.silentgo.core.build.Factory;
import com.silentgo.core.exception.AppReleaseException;
import com.silentgo.core.plugin.db.bridge.BaseTableInfo;
import com.silentgo.core.plugin.db.bridge.TableModel;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.utils.CollectionKit;

import java.util.HashMap;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.plugin.db.bridge
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/23.
 */
@Factory
public class SqlFactory extends BaseFactory {
    private Map<Class<? extends TableModel>, BaseTableInfo> tableInfo = new HashMap<>();

    public BaseTableInfo getTableInfo(Class<? extends TableModel> clz) {
        return tableInfo.get(clz);
    }

    public void addTableInfo(Class<? extends TableModel> clz, BaseTableInfo info) {
        CollectionKit.MapAdd(tableInfo, clz, info);
    }

    @Override
    public boolean initialize(SilentGo me) {
        if (me.getConfig().getDbType() == null) return true;
        me.getConfig().addAbstractConfig(new DBConfig(me.getConfig().getDbType(), "application.properties"));

        return true;
    }

    @Override
    public boolean destroy(SilentGo me) throws AppReleaseException {
        return false;
    }
}
