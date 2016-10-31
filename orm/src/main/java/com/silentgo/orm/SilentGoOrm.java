package com.silentgo.orm;

import com.silentgo.orm.base.DBConnect;
import com.silentgo.orm.base.NameParam;
import com.silentgo.orm.common.Const;
import com.silentgo.orm.rsresolver.IRSResolver;
import com.silentgo.orm.rsresolver.support.*;
import com.silentgo.utils.TypeConvertKit;
import com.silentgo.utils.logger.Logger;
import com.silentgo.utils.logger.LoggerFactory;

import java.sql.*;
import java.util.List;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.orm
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/20.
 */
public class SilentGoOrm {
    private static Logger LOGGER = LoggerFactory.getLog(SilentGoOrm.class);

    private static boolean pmdKnownBroken = false;

    public static <T> Object excute(DBConnect conn, String sql, Class<T> clz, String type, boolean isList, boolean isArray, Object[] generateKeys, Object[] params)
            throws SQLException {
        if (conn == null) {
            throw new SQLException("Null connection");
        }

        if (sql == null) {
            throw new SQLException("Null SQL statement");
        }
        IRSResolver resolver = getResolver(clz, isList, isArray);
        if (clz == null || resolver == null) {
            throw new SQLException("Null Class");
        }

        PreparedStatement stmt = null;
        ResultSet rs = null;
        type = type.toLowerCase();
        try {
            if (Const.Insert.equals(type)) {
                stmt = conn.getConnect().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            } else {
                stmt = conn.getConnect().prepareStatement(sql);
            }

            fillStatement(stmt, params);

            if (Const.Query.equals(type)) {
                rs = stmt.executeQuery();
            } else if (Const.Insert.equals(type)) {
                int rows = stmt.executeUpdate();
                rs = stmt.getGeneratedKeys();
                if (generateKeys != null) {
                    Object[] keys = new ArrayRSResolver().resolve(rs);
                    for (int i = 0; i < generateKeys.length && i < keys.length; i++) {
                        generateKeys[i] = keys[i];
                    }
                }
                return rows;
            } else {
                return stmt.executeUpdate();
            }
            return resolver.resolve(rs);
        } finally {
            finalDo(rs, stmt, conn);
        }

    }

    public static <T> int updateOrDelete(DBConnect conn, String sql, String type, Class<T> clz, Object[] params) throws SQLException {
        return (int) excute(conn, sql, clz, type, false, false, null, params);
    }

    public static <T> int insert(DBConnect conn, String sql, Class<T> clz, Object[] generateKeys, Object[] params) throws SQLException {
        return (int) excute(conn, sql, clz, Const.Insert, false, false, generateKeys, params);
    }

    public static <T> T query(DBConnect conn, String sql, Class<T> clz, Object[] params) throws SQLException {
        return (T) excute(conn, sql, clz, Const.Query, false, false, null, params);
    }

    public static <T> List<T> queryList(DBConnect conn, String sql, Class<T> clz, Object[] params)
            throws SQLException {
        return (List<T>) excute(conn, sql, clz, Const.Query, true, false, null, params);
    }


    public static <T> T[] queryArray(DBConnect conn, String sql, Class<T> clz, Object[] params) throws SQLException {
        return (T[]) excute(conn, sql, clz, Const.Query, false, true, null, params);
    }

    public static <T> List<T[]> queryArrayList(DBConnect conn, String sql, Class<T> clz, Object[] params) throws SQLException {
        return (List<T[]>) excute(conn, sql, clz, Const.Query, true, true, null, params);
    }


    private static IRSResolver getResolver(Class<?> clz, boolean isList, boolean isArray) {
        if (TypeConvertKit.isSqlBaseType(clz)) {
            return isList ? new ListCompatibleRSResolver(clz) : new CompatibleRSResolver(clz);
        } else if (isArray) {
            return isList ? new ListArrayRSResolver() : new ArrayRSResolver();
        } else if (Map.class.isAssignableFrom(clz)) {
            return isList ? new ListMapRSResolver() : new MapRSResolver();
        } else {
            return isList ? new ListBeanRSResolver(clz) : new BeanRSResolver(clz);
        }
    }

    private static void fillStatement(PreparedStatement stmt, Object... params)
            throws SQLException {

        // check the parameter count, if we can
        ParameterMetaData pmd = null;
        if (!pmdKnownBroken) {
            pmd = stmt.getParameterMetaData();
            int stmtCount = pmd.getParameterCount();
            int paramsCount = params == null ? 0 : params.length;

            if (stmtCount != paramsCount) {
                throw new SQLException("Wrong number of parameters: expected "
                        + stmtCount + ", was given " + paramsCount);
            }
        }

        // nothing to do here
        if (params == null) {
            return;
        }
        for (int i = 0; i < params.length; i++) {
            if (params[i] != null) {
                stmt.setObject(i + 1, params[i]);
            } else {
                // VARCHAR works with many drivers regardless
                // of the actual column type. Oddly, NULL and
                // OTHER don't work with Oracle's drivers.
                int sqlType = Types.VARCHAR;
                if (!pmdKnownBroken) {
                    try {
                        /*
                         * It's not possible for pmdKnownBroken to change from
                         * true to false, (once true, always true) so pmd cannot
                         * be null here.
                         */
                        sqlType = pmd.getParameterType(i + 1);
                    } catch (SQLException e) {
                        pmdKnownBroken = true;
                    }
                }
                stmt.setNull(i + 1, sqlType);
            }
        }
    }


    private static void finalDo(ResultSet rs, PreparedStatement statement, DBConnect connection) throws SQLException {
        try {
            if (rs != null)
                rs.close();
        } finally {
            if (statement != null)
                statement.close();
        }
    }
}
