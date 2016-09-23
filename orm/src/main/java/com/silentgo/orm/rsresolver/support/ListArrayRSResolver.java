package com.silentgo.orm.rsresolver.support;

import com.silentgo.orm.rsresolver.base.ListRowRSResolver;

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
public class ListArrayRSResolver implements ListRowRSResolver<Object[]> {

    private Integer cols;

    @Override
    public Object[] resolveRow(ResultSet resultSet) throws SQLException {

        if (cols == null)
            cols = resultSet.getMetaData().getColumnCount();

        return BaseResolverKit.resolveArray(resultSet, cols);
    }
}
