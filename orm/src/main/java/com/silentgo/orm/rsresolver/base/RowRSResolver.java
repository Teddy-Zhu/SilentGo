package com.silentgo.orm.rsresolver.base;

import com.silentgo.orm.rsresolver.IRSResolver;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Project : silentgo
 * com.silentgo.orm.rsresolver.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/21.
 */
public interface RowRSResolver<O, T> extends IRSResolver<T> {

    public O resolveRow(ResultSet resultSet) throws SQLException;
}
