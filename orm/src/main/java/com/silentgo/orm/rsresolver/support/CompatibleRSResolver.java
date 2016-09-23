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
public class CompatibleRSResolver<T> implements OneRowRSResolver<T> {


    private Class<T> clz;

    private Integer index;
    private String name;

    public CompatibleRSResolver(Class<T> clz, String name) {
        this.clz = clz;
        this.name = name;
    }

    public CompatibleRSResolver(Class<T> clz, Integer index) {
        this.clz = clz;
        this.index = index;
    }

    public CompatibleRSResolver(Class<T> clz) {
        this.clz = clz;
        this.index = 1;
    }

    @Override
    public T resolveRow(ResultSet resultSet) throws SQLException {
        return index != null ? BaseResolverKit.processColumn(resultSet, index, clz) : BaseResolverKit.processColumn(resultSet, name, clz);
    }
}
