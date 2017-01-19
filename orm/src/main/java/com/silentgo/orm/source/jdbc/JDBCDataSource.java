package com.silentgo.orm.source.jdbc;

import com.silentgo.orm.base.DBConfig;
import com.silentgo.orm.base.DBDataSource;
import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * Project : silentgo
 * com.silentgo.orm
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/13.
 */
public class JDBCDataSource implements DBDataSource {

    private static final Log LOGGER = LogFactory.get();
    private String name;

    private DBConfig config;

    public JDBCDataSource(String name, DBConfig config) {
        try {
            Class.forName(config.getDriver());

        } catch (ClassNotFoundException e) {
            LOGGER.info("can not found mysql jdbc driver");
            e.printStackTrace();
        }
        this.name = name;
        this.config = config;
    }

    @Override
    public DBConfig getConfig() {
        return config;
    }

    @Override
    public void setConfig(DBConfig config) {
        this.config = config;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(config.getUrl(),
                config.getUserName(),
                config.getPassword());
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return DriverManager.getConnection(config.getUrl(), username, password);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 5000;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }


}
