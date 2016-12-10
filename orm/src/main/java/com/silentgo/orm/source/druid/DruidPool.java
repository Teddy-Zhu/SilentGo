package com.silentgo.orm.source.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.silentgo.orm.base.DBConnect;
import com.silentgo.orm.base.DBPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * Project : SilentGo
 * Package : com.silentgo.orm.source.druid
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/12/5.
 */
public class DruidPool implements DBPool {

    private static final Logger LOGGER = LoggerFactory.getLogger(DruidPool.class);
    private ThreadLocal<DBConnect> threadConnect = new ThreadLocal<>();

    private DruidDataSource ds;

    public DruidPool(DruidDataSource ds) {
        this.ds = ds;
    }

    @Override
    public synchronized DBConnect getDBConnect() {
        try {
            DBConnect connect = threadConnect.get();
            if (connect == null) {
                connect = new DruidConnect(ds.getConnection());
                threadConnect.set(connect);
            } else {
                if (!connect.getConnect().isValid(3000) || connect.getConnect().isClosed()) {
                    connect = new DruidConnect(ds.getConnection());
                    threadConnect.set(connect);
                }
            }
            return connect;
        } catch (SQLException e) {
            LOGGER.error("get druid connect error", e);
            return null;
        }
    }

    @Override
    public boolean releaseDBConnect(DBConnect connect) {
        DBConnect connect1 = threadConnect.get();
        connect1.close();
        threadConnect.remove();
        return true;
    }

    @Override
    public boolean destory() {
        ds.close();
        return true;
    }

    @Override
    public DBConnect getUnSafeDBConnect() {
        try {
            return new DruidConnect(ds.getConnection());
        } catch (SQLException e) {
            LOGGER.error("get unsafe druid connect error", e);
            return null;
        }
    }

    @Override
    public boolean releaseUnSafeDBConnect(DBConnect connect) {
        connect.close();
        return true;
    }

    @Override
    public DBConnect getThreadConnect() {
        return threadConnect.get();
    }

    @Override
    public boolean setThreadConnect(DBConnect connect) {
        threadConnect.set(connect);
        return true;
    }
}
