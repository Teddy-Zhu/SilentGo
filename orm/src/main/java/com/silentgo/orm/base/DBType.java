package com.silentgo.orm.base;

/**
 * Project : silentgo
 * com.silentgo.core.plugin.db
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/22.
 */
public enum DBType {
    MYSQL("mysql");
    private String name;

    DBType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public boolean equals(String name) {
        return this.name.equals(name);
    }

    public static DBType parse(String name) {

        try {
            return Enum.valueOf(DBType.class, name.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }
}
