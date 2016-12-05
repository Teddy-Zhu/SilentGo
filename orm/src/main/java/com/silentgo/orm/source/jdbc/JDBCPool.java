package com.silentgo.orm.source.jdbc;

import com.silentgo.orm.base.DBConnect;
import com.silentgo.orm.base.DBPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Project : silentgo
 * com.silentgo.orm
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/13.
 */
public class JDBCPool implements DBPool {

    private ThreadLocal<DBConnect> threadConnect = new InheritableThreadLocal<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCPool.class);

    private String name;

    private JDBCDataSource source;

    private Stack<JDBCConnect> connects = new Stack<>();

    private List<JDBCConnect> unSafeConnect = Collections.synchronizedList(new ArrayList<>());

    public JDBCPool(JDBCDataSource source) {
        this.name = source.getName();
        this.source = source;
        for (int i = 0, len = source.getConfig().getMinActive(); i < len; i++) {
            LOGGER.info("init connect index :{}", connects.size());
            createConnect();
        }
    }

    @Override
    public synchronized DBConnect getDBConnect() {
        DBConnect connect = threadConnect.get();
        if (connect == null) {
            connect = useConnect();
            threadConnect.set(connect);
        }
        return connect;
    }

    @Override
    public boolean releaseDBConnect(DBConnect connect) {
        threadConnect.remove();
        restoreConnect(connect);
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

    @Override
    public DBConnect getUnSafeDBConnect() {
        JDBCConnect connect = useConnect();
        unSafeConnect.add(connect);
        return connect;
    }

    @Override
    public boolean releaseUnSafeDBConnect(DBConnect connect) {
        unSafeConnect.remove(connect);
        restoreConnect(connect);
        return false;
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

    private void restoreConnect(DBConnect connect) {
        try {
            connect.getConnect().setAutoCommit(true);
            connect.getConnect().setTransactionIsolation(source.getConfig().getDefaultTranscationLevel());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connects.push((JDBCConnect) connect);
    }

    private JDBCConnect useConnect() {
        JDBCConnect connect = null;
        try {
            connect = connects.pop();
            if (connect != null) {
                if (connect.getEnd().getTime() <= new Date().getTime() || !connect.getConnect().isValid(1000)) {
                    connect.close();
                    connect = null;
                }
            }
        } catch (EmptyStackException | SQLException e) {
            connect = null;
            //try create connect
            createConnect();
        }
        return connect == null ? useConnect() : connect;
    }

    public synchronized JDBCConnect createConnect() {
        LOGGER.debug("size:{}", connects.size());
        if (connects.size() >= source.getConfig().getMaxActive()) {
            return null;
        }
        Connection connect = null;
        try {
            connect = source.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JDBCConnect connect1 = new JDBCConnect(connect, source.getConfig());
        connects.push(connect1);
        return connect1;
    }
}
