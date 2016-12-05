package com.silentgo.orm.source.jdbc;

import com.silentgo.orm.base.DBConfig;
import com.silentgo.orm.base.DBConnect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.silentgo.utils.random.RandomUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

/**
 * Project : silentgo
 * com.silentgo.orm.source.jdbc
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/13.
 */
public class JDBCConnect implements DBConnect {

    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCConnect.class);

    private String name;

    private Date start;
    private Date end;

    private int maxIdle;
    private int timeOut;
    private Connection connection;


    public JDBCConnect(Connection connection, DBConfig config) {
        this.start = new Date();
        this.name = RandomUtil.String(10);
        this.maxIdle = config.getMaxIdle();
        this.end = new Date(this.start.getTime() + maxIdle);
        this.connection = connection;
        this.timeOut = config.getTimeOut();
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean isValid() {
        return new Date().getTime() < end.getTime();
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Connection getConnect() {
        return connection;
    }

    @Override
    public boolean close() {
        try {
            if (connection != null && !connection.isClosed())
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
