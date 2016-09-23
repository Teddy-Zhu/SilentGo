package com.silentgo.orm.base;

/**
 * Project : silentgo
 * com.silentgo.orm
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/13.
 */
public class DataSource {

    private String name;

    private DBConfig config;


    public DataSource(String name, DBConfig config) {
        this.name = name;
        this.config = config;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DBConfig getConfig() {
        return config;
    }

    public void setConfig(DBConfig config) {
        this.config = config;
    }
}
