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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    private List<JDBCConnect> connects;

    public JDBCPool(DataSource source) {
        this.name = source.getName();
        try {
            Class.forName(source.getConfig().getDriver());

        } catch (ClassNotFoundException e) {
            LOGGER.info("can not found mysql jdbc driver");
            e.printStackTrace();
        }
        this.source = source;
        connects = Collections.synchronizedList(new ArrayList<>());
        for (int i = 0, len = source.getConfig().getMinActive(); i < len; i++) {
            LOGGER.info("init:{}", connects.size());
            createConnect(source.getConfig());
        }
    }

    public DataSource getSource() {
        return source;
    }

    public void setSource(DataSource source) {
        this.source = source;
    }

    @Override
    public DBConnect getDBConnect() {
        return useConnect();
    }

    @Override
    public boolean destory() {
        List<JDBCConnect> des = connects;

        return des.stream().allMatch(c -> {
            c.use();
            return c.destroy();
        });
    }

    public JDBCConnect useConnect() {
        Optional<JDBCConnect> connectOptional = connects.stream()
                .filter(connect -> !connect.isUsed()).sorted((o1, o2) -> {
                    long x = o1.getEnd().getTime();
                    long y = o2.getEnd().getTime();
                    return (x < y) ? -1 : ((x == y) ? 0 : 1);
                }).findFirst();
        if (connectOptional.isPresent()) {
            if (connectOptional.get().use()) {
                return connectOptional.get();
            } else {
                return useConnect();
            }
        } else {
            JDBCConnect connect = createConnect(source.getConfig());
            if (connect != null) {
                if (connect.use()) {
                    return connect;
                } else {
                    return useConnect();
                }
            } else {
                LOGGER.info("wait lock connect");
                return useConnect();
            }
        }
    }

    public synchronized JDBCConnect createConnect(DBConfig config) {
        LOGGER.info("size:{}", connects.size());
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
        connects.add(connect1);
        return connect1;
    }
}
