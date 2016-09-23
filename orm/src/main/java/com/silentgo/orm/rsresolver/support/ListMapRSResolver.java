package com.silentgo.orm.rsresolver.support;

import com.silentgo.orm.rsresolver.base.ListRowRSResolver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.orm.rsresolver.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/21.
 */
public class ListMapRSResolver implements ListRowRSResolver<Map<String,Object>> {

    @Override
    public Map<String, Object> resolveRow(ResultSet resultSet) throws SQLException {
        return BaseResolverKit.resolveMap(resultSet);
    }
}
