package com.silentgo.core.plugin.db;

import java.util.List;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.plugin.db.bridge
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/22.
 */
public abstract class BaseTableInfo {

    public String tableName;
    public List<String> primaryKeys;
    public Map<String, Class<?>> columnsMap;

    public Map<String,String> fullColumns;

    public Map<String, String> getFullColumns() {
        return fullColumns;
    }

    public void setFullColumns(Map<String, String> fullColumns) {
        this.fullColumns = fullColumns;
    }

    public List<String> getPrimaryKeys() {
        return primaryKeys;
    }

    public void setPrimaryKeys(List<String> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Map<String, Class<?>> getColumnsMap() {
        return columnsMap;
    }

    public void setColumnsMap(Map<String, Class<?>> columnsMap) {
        this.columnsMap = columnsMap;
    }
}
