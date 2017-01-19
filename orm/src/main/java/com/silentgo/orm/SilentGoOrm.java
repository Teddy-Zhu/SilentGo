package com.silentgo.orm;

import com.silentgo.orm.base.DBConnect;
import com.silentgo.orm.common.Const;
import com.silentgo.orm.kit.SQLExcuteKit;
import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;

import java.sql.*;
import java.util.List;

/**
 * Project : silentgo
 * com.silentgo.orm
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/20.
 */
public class SilentGoOrm {
    private static final Log LOGGER = LogFactory.get();
    public static <T> int updateOrDelete(DBConnect conn, String sql, String type, Class<T> clz, Object[] params) throws SQLException {
        return (int) SQLExcuteKit.execute(conn, sql, clz, type, false, false, null, params);
    }

    public static <T> int insert(DBConnect conn, String sql, Class<T> clz, Object[] generateKeys, Object[] params) throws SQLException {
        return (int) SQLExcuteKit.execute(conn, sql, clz, Const.Insert, false, false, generateKeys, params);
    }

    public static <T> T query(DBConnect conn, String sql, Class<T> clz, Object[] params) throws SQLException {
        return (T) SQLExcuteKit.execute(conn, sql, clz, Const.Query, false, false, null, params);
    }

    public static <T> List<T> queryList(DBConnect conn, String sql, Class<T> clz, Object[] params)
            throws SQLException {
        return (List<T>) SQLExcuteKit.execute(conn, sql, clz, Const.Query, true, false, null, params);
    }


    public static <T> T[] queryArray(DBConnect conn, String sql, Class<T> clz, Object[] params) throws SQLException {
        return (T[]) SQLExcuteKit.execute(conn, sql, clz, Const.Query, false, true, null, params);
    }

    public static <T> List<T[]> queryArrayList(DBConnect conn, String sql, Class<T> clz, Object[] params) throws SQLException {
        return (List<T[]>) SQLExcuteKit.execute(conn, sql, clz, Const.Query, true, true, null, params);
    }

}
