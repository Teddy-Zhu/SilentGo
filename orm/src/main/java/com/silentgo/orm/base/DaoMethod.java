package com.silentgo.orm.base;

/**
 * Project : parent
 * Package : com.silentgo.core.db
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/6.
 */
public class DaoMethod {
    private boolean isList;
    private boolean isArray;
    private Class<?> type;

    public DaoMethod() {
        isList = false;
        isArray = false;
    }

    public boolean isList() {
        return isList;
    }

    public void setList(boolean list) {
        isList = list;
    }

    public boolean isArray() {
        return isArray;
    }

    public void setArray(boolean array) {
        isArray = array;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }
}
