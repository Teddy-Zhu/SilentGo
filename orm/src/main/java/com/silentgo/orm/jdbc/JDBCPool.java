package com.silentgo.orm.jdbc;

import com.silentgo.orm.base.DBConfig;
import com.silentgo.orm.base.DBConnect;
import com.silentgo.orm.base.DBPool;
import com.silentgo.orm.base.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

/**
 * Project : silentgo
 * com.silentgo.orm
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/13.
 */
public class JDBCPool implements DBPool {

    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCPool.class);
    /**
     * 连接池名称
     */
    private String name;
    /**
     * 数据源
     */
    private DataSource source;

    private Stack<JDBCConnect> connects;

    public JDBCPool(DataSource source) {
        this.name = source.getName();
        try {
            Class.forName(source.getConfig().getDriver());

        } catch (ClassNotFoundException e) {
            LOGGER.info("can not found mysql jdbc driver");
            e.printStackTrace();
        }
        this.source = source;
        connects = new Stack<>();
        for (int i = 0, len = source.getConfig().getMinActive(); i < len; i++) {
            LOGGER.info("init:{}", connects.size());
            createConnect(source.getConfig());
        }
    }

    @Override
    public DBConnect getDBConnect() {
        return useConnect();
    }

    @Override
    public boolean releaseDBConnect(DBConnect connect) {
        connects.push((JDBCConnect) connect);
        return true;
    }

    @Override
    public boolean destory() {
        List<JDBCConnect> des = connects;

        return des.stream().allMatch(c -> {
            try {
                c.getConnect().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return true;
        });
    }

    public JDBCConnect useConnect() {
        JDBCConnect connect = null;
        try {
            connect = connects.pop();
            if (connect != null) {
                if (connect.getEnd().getTime() <= new Date().getTime() || !connect.getConnect().isValid(1000)) {
                    connect.destroy();
                    connect = null;
                }
            }
        } catch (EmptyStackException | SQLException e) {
            connect = null;
            //try create connect
            createConnect(source.getConfig());
        }
        return connect == null ? useConnect() : connect;
    }

    public synchronized JDBCConnect createConnect(DBConfig config) {
        LOGGER.debug("size:{}", connects.size());
        if (connects.size() >= source.getConfig().getMaxActive()) {
            return null;
        }
        Connection connect = null;
        try {
            connect = DriverManager.getConnection(source.getConfig().getUrl(),
                    source.getConfig().getUserName(),
                    source.getConfig().getPassword());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JDBCConnect connect1 = new JDBCConnect(connect, config);
        connects.push(connect1);
        return connect1;
    }
}
