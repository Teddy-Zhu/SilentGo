package com.silentgo.orm.jdbc;

import com.silentgo.orm.DBPool;
import com.silentgo.orm.DataSource;
import com.silentgo.utils.logger.Logger;
import com.silentgo.utils.logger.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Project : silentgo
 * com.silentgo.orm
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/13.
 */
public class JDBCPool implements DBPool {

    private static final Logger LOGGER = LoggerFactory.getLog(JDBCPool.class);
    /**
     * 连接池名称
     */
    private String name;
    /**
     * 数据源
     */
    private DataSource source;

    private List<JDBCConnect> connects;

    public JDBCPool(DataSource source) {
        this.name = source.getName();
        try {
            Class.forName(source.getConfig().getDriver());

            connects = new ArrayList<>();
            for (int i = 0, len = source.getConfig().getMinActive(); i < len; i++) {
                Connection connect = DriverManager.getConnection(source.getConfig().getUrl(),
                        source.getConfig().getUserName(),
                        source.getConfig().getPassword());
                connects.add(new JDBCConnect(new Date(), new Date(), connect));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("can not create connect");
        } catch (ClassNotFoundException e) {
            LOGGER.info("can not found mysql jdbc driver");
            e.printStackTrace();
        }
        this.source = source;
    }

    public DataSource getSource() {
        return source;
    }

    public void setSource(DataSource source) {
        this.source = source;
    }

    @Override
    public synchronized Object getConnect() {
        return null;
    }
}
