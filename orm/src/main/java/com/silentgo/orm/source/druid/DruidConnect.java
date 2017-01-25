package com.silentgo.orm.source.druid;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.silentgo.orm.base.DBConnect;
import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;

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

    private static final Log LOGGER = LogFactory.get();
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
            LOGGER.info("druid debug ------ close connect : {} ", this);
            connection.close();
        } catch (SQLException e) {
            LOGGER.error(e, "druid close connect error");
        }
        return true;
    }
}
