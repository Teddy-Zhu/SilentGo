package com.silentgo.orm.rsresolver.base;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Project : silentgo
 * com.silentgo.orm.rsresolver.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/20.
 */
public interface ListRowRSResolver<T> extends RowRSResolver<T, List<T>> {

    @Override
    default List<T> resolve(ResultSet resultSet) throws SQLException {
        List<T> ret = new ArrayList<T>();
        while (resultSet.next()) {
            ret.add(this.resolveRow(resultSet));
        }
        return ret;
    }
}
