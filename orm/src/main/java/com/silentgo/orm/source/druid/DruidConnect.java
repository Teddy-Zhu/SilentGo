package com.silentgo.orm.source.druid;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.silentgo.orm.base.DBConnect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Project : SilentGo
 * Package : com.silentgo.orm.source.druid
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/12/5.
 */
public class DruidConnect implements DBConnect {

    private static final Logger LOGGER = LoggerFactory.getLogger(DruidConnect.class);

    DruidPooledConnection connection;

    public DruidConnect(DruidPooledConnection connection) {
        this.connection = connection;
    }

    @Override
    public Connection getConnect() {
        return connection;
    }

    @Override
    public boolean close() {
        try {
            connection.close();
        } catch (SQLException e) {
            LOGGER.error("druid close connect error", e);
        }
        return true;
    }
}
