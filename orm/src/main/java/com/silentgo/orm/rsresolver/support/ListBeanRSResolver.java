package com.silentgo.orm.rsresolver.support;

import com.silentgo.orm.rsresolver.base.ListRowRSResolver;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.orm.rsresolver.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/21.
 */
public class ListBeanRSResolver<T> implements ListRowRSResolver<T> {
    private static final Map<Class<?>, Object> primitiveDefaults = new HashMap<Class<?>, Object>();

    static {
        primitiveDefaults.put(Integer.TYPE, Integer.valueOf(0));
        primitiveDefaults.put(Short.TYPE, Short.valueOf((short) 0));
        primitiveDefaults.put(Byte.TYPE, Byte.valueOf((byte) 0));
        primitiveDefaults.put(Float.TYPE, Float.valueOf(0f));
        primitiveDefaults.put(Double.TYPE, Double.valueOf(0d));
        primitiveDefaults.put(Long.TYPE, Long.valueOf(0L));
        primitiveDefaults.put(Boolean.TYPE, Boolean.FALSE);
        primitiveDefaults.put(Character.TYPE, Character.valueOf((char) 0));
    }

    private Class<T> clz;

    private int[] columnToProperty;

    private PropertyDescriptor[] props;
    private final Map<String, String> columnToPropertyOverrides;


    public ListBeanRSResolver(Class<T> clz) {
        this(clz, new HashMap<String, String>());
    }

    public ListBeanRSResolver(Class<T> clz, Map<String, String> columnToPropertyOverrides) {
        super();
        if (clz == null) {
            throw new IllegalArgumentException("class in bean resolver cannot be null");
        }
        if (columnToPropertyOverrides == null) {
            throw new IllegalArgumentException("columnToPropertyOverrides map cannot be null");
        }
        this.clz = clz;
        this.columnToPropertyOverrides = columnToPropertyOverrides;
    }

    @Override
    public T resolveRow(ResultSet resultSet) throws SQLException {
        if (props == null)
            props = BaseResolverKit.propertyDescriptors(clz);

        if (columnToProperty == null) {
            ResultSetMetaData rsmd = resultSet.getMetaData();
            columnToProperty = BaseResolverKit.mapColumnsToProperties(rsmd, props, columnToPropertyOverrides);
        }

        return BaseResolverKit.resolveBean(resultSet, clz, props, columnToProperty, primitiveDefaults);

    }
}
