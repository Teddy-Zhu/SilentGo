package com.silentgo.orm.base;

/**
 * Project : SilentGo
 * Package : com.silentgo.orm.base
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/24.
 */
public class Column {

    private String propName;

    private String columnName;

    private String fullName;

    private String selectFullName;

    private Class<?> type;

    public Column() {
    }

    public Column(String fullName, String selectfullName) {
        this.fullName = fullName;
        this.selectFullName = selectfullName;
    }

    public Column(String propName, String columnName, String fullName, Class<?> type) {
        this.propName = propName;
        this.columnName = columnName;
        this.fullName = fullName;
        this.selectFullName = fullName + (propName.equals(columnName) ? "" : " as " + propName);
        this.type = type;
    }

    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public String getSelectFullName() {
        return selectFullName;
    }

    public void setSelectFullName(String selectFullName) {
        this.selectFullName = selectFullName;
    }
}
