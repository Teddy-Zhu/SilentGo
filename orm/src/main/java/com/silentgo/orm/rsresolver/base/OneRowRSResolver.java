package com.silentgo.orm.rsresolver.base;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Project : silentgo
 * com.silentgo.orm.rsresolver.base
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/21.
 */
public interface OneRowRSResolver<T> extends RowRSResolver<T, T> {


    default T getNull() {
        return null;
    }

    @Override
    default T resolve(ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) return getNull();
        return resolveRow(resultSet);
    }

}
