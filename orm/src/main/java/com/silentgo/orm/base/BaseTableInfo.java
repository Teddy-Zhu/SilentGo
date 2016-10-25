package com.silentgo.orm.base;

import com.silentgo.orm.base.DBType;
import com.silentgo.orm.base.TableModel;

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
public class BaseTableInfo {

    public DBType type;
    public Class<? extends TableModel> clazz;
    public String tableName;
    public List<String> primaryKeys;
    public Map<String, Column> columnInfo;

    public Class<? extends TableModel> getClazz() {
        return clazz;
    }

    public DBType getType() {
        return type;
    }

    public void setType(DBType type) {
        this.type = type;
    }

    public void setClazz(Class<? extends TableModel> clazz) {
        this.clazz = clazz;
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

    public Map<String, Column> getColumnInfo() {
        return columnInfo;
    }

    public Column get(String name) {
        return columnInfo.get(name);
    }

    public void setColumnInfo(Map<String, Column> columnInfo) {
        this.columnInfo = columnInfo;
    }
}
