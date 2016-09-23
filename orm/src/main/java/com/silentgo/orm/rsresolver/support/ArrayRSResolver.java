package com.silentgo.orm.rsresolver.support;

import com.silentgo.orm.rsresolver.base.OneRowRSResolver;

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
public class ArrayRSResolver implements OneRowRSResolver<Object[]> {

    protected static final Object[] EMPTY_ARRAY = new Object[0];

    private Integer cols = null;

    @Override
    public Object[] getNull() {
        return EMPTY_ARRAY;
    }

    @Override
    public Object[] resolveRow(ResultSet resultSet) throws SQLException {
        if (cols == null)
            cols = resultSet.getMetaData().getColumnCount();

        return BaseResolverKit.resolveArray(resultSet, cols);
    }
}
