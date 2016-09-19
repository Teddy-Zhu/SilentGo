package com.silentgo.orm.jdbc;

import java.sql.Connection;
import java.util.Date;

/**
 * Project : silentgo
 * com.silentgo.orm.jdbc
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/13.
 */
public class JDBCConnect {

    private Date start;
    private Date end;

    private boolean used = false;

    private Connection connection;

    private Date lastAlive;

    public boolean isUsed() {
        return used;
    }

    public JDBCConnect(Date start, Date end, Connection connection) {
        this.start = start;
        this.end = end;
        this.connection = connection;
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
        lastAlive = new Date();
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
