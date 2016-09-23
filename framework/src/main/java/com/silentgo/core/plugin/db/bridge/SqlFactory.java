package com.silentgo.core.plugin.db.bridge;

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
public class SqlFactory extends BaseFactory {
    private Map<Class<? extends TableModel>, BaseTableInfo> tableInfo = new HashMap<>();

    public BaseTableInfo getTableInfo(Class<? extends TableModel> clz) {
        return tableInfo.get(clz);
    }

    public void addTableInfo(Class<? extends TableModel> clz, BaseTableInfo info) {
        CollectionKit.MapAdd(tableInfo, clz, info);
    }
}
