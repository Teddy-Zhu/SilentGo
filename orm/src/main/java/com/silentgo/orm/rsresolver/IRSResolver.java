package com.silentgo.orm.rsresolver;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Project : silentgo
 * com.silentgo.orm.rsresolver
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/20.
 */
public interface IRSResolver<T> {

    T resolve(ResultSet resultSet) throws SQLException;
}
