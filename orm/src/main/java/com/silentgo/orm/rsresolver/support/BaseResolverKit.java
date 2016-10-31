package com.silentgo.orm.rsresolver.support;

import com.silentgo.orm.common.CaseInsensitiveHashMap;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Arrays;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.orm.rsresolver.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/21.
 */
public class BaseResolverKit {

    public static Object[] resolveArray(ResultSet rs, Integer cols) throws SQLException {
        Object[] ret = new Object[cols];
        for (int i = 0; i < cols; i++) {
            ret[i] = rs.getObject(i + 1);
        }
        return ret;
    }

    public static final int PROPERTY_NOT_FOUND = -1;

    public static <T> T resolveBean(ResultSet rs, Class<T> type,
                                    PropertyDescriptor[] props, int[] columnToProperty,
                                    Map<Class<?>, Object> primitiveDefaults)
            throws SQLException {

        T bean = null;
        try {
            bean = type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        for (int i = 1; i < columnToProperty.length; i++) {

            if (columnToProperty[i] == PROPERTY_NOT_FOUND) {
                continue;
            }

            PropertyDescriptor prop = props[columnToProperty[i]];
            Class<?> propType = prop.getPropertyType();

            Object value = null;
            if (propType != null) {
                value = processColumn(rs, i, propType);

                if (value == null && propType.isPrimitive()) {
                    value = primitiveDefaults.get(propType);
                }
            }

            callSetter(bean, prop, value);
        }

        return bean;
    }

    public static Map<String, Object> resolveMap(ResultSet resultSet) throws SQLException {
        Map<String, Object> result = new CaseInsensitiveHashMap();
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int cols = rsmd.getColumnCount();

        for (int i = 1; i <= cols; i++) {
            String columnName = rsmd.getColumnLabel(i);
            if (null == columnName || 0 == columnName.length()) {
                columnName = rsmd.getColumnName(i);
            }
            result.put(columnName, resultSet.getObject(i));
        }
        return result;
    }


    public static <T> T processColumn(ResultSet rs, String name, Class<T> propType)
            throws SQLException {


        if (!propType.isPrimitive() && rs.getObject(name) == null) {
            return null;
        }

        if (propType.equals(String.class)) {
            return (T) rs.getString(name);

        } else if (
                propType.equals(Integer.TYPE) || propType.equals(Integer.class)) {
            return (T) Integer.valueOf(rs.getInt(name));

        } else if (
                propType.equals(Boolean.TYPE) || propType.equals(Boolean.class)) {
            return (T) Boolean.valueOf(rs.getBoolean(name));

        } else if (propType.equals(Long.TYPE) || propType.equals(Long.class)) {
            return (T) Long.valueOf(rs.getLong(name));

        } else if (
                propType.equals(Double.TYPE) || propType.equals(Double.class)) {
            return (T) Double.valueOf(rs.getDouble(name));

        } else if (
                propType.equals(Float.TYPE) || propType.equals(Float.class)) {
            return (T) Float.valueOf(rs.getFloat(name));

        } else if (
                propType.equals(Short.TYPE) || propType.equals(Short.class)) {
            return (T) Short.valueOf(rs.getShort(name));

        } else if (propType.equals(Byte.TYPE) || propType.equals(Byte.class)) {
            return (T) Byte.valueOf(rs.getByte(name));

        } else if (propType.equals(BigDecimal.class)) {
            return (T) rs.getBigDecimal(name);
        } else if (propType.equals(Timestamp.class)) {
            return (T) rs.getTimestamp(name);

        } else if (propType.equals(SQLXML.class)) {
            return (T) rs.getSQLXML(name);

        } else {
            return (T) rs.getObject(name);
        }
    }

    public static <T> T processColumn(ResultSet rs, int index, Class<T> propType)
            throws SQLException {

        if (!propType.isPrimitive() && rs.getObject(index) == null) {
            return null;
        }

        if (propType.equals(String.class)) {
            return (T) rs.getString(index);

        } else if (
                propType.equals(Integer.TYPE) || propType.equals(Integer.class)) {
            return (T) Integer.valueOf(rs.getInt(index));

        } else if (
                propType.equals(Boolean.TYPE) || propType.equals(Boolean.class)) {
            return (T) Boolean.valueOf(rs.getBoolean(index));

        } else if (propType.equals(Long.TYPE) || propType.equals(Long.class)) {
            return (T) Long.valueOf(rs.getLong(index));

        } else if (
                propType.equals(Double.TYPE) || propType.equals(Double.class)) {
            return (T) Double.valueOf(rs.getDouble(index));

        } else if (
                propType.equals(Float.TYPE) || propType.equals(Float.class)) {
            return (T) Float.valueOf(rs.getFloat(index));

        } else if (
                propType.equals(Short.TYPE) || propType.equals(Short.class)) {
            return (T) Short.valueOf(rs.getShort(index));

        } else if (propType.equals(Byte.TYPE) || propType.equals(Byte.class)) {
            return (T) Byte.valueOf(rs.getByte(index));

        } else if (propType.equals(BigDecimal.class)) {
            return (T) rs.getBigDecimal(index);
        } else if (propType.equals(Timestamp.class)) {
            return (T) rs.getTimestamp(index);

        } else if (propType.equals(SQLXML.class)) {
            return (T) rs.getSQLXML(index);

        } else {
            return (T) rs.getObject(index);
        }

    }

    private static void callSetter(Object target, PropertyDescriptor prop, Object value)
            throws SQLException {

        Method setter = prop.getWriteMethod();

        if (setter == null) {
            return;
        }

        Class<?>[] params = setter.getParameterTypes();
        try {
            // convert types for some popular ones
            if (value instanceof java.util.Date) {
                final String targetType = params[0].getName();
                if ("java.sql.Date".equals(targetType)) {
                    value = new java.sql.Date(((java.util.Date) value).getTime());
                } else if ("java.sql.Time".equals(targetType)) {
                    value = new java.sql.Time(((java.util.Date) value).getTime());
                } else if ("java.sql.Timestamp".equals(targetType)) {
                    Timestamp tsValue = (Timestamp) value;
                    int nanos = tsValue.getNanos();
                    value = new Timestamp(tsValue.getTime());
                    ((Timestamp) value).setNanos(nanos);
                }
            } else if (value instanceof String && params[0].isEnum()) {
                value = Enum.valueOf(params[0].asSubclass(Enum.class), (String) value);
            }

            // Don't call setter if the value object isn't the right type
            if (isCompatibleType(value, params[0])) {
                setter.invoke(target, new Object[]{value});
            } else {
                throw new SQLException(
                        "Cannot setEqual " + prop.getName() + ": incompatible types, cannot convert "
                                + value.getClass().getName() + " to " + params[0].getName());
                // value cannot be null here because isCompatibleType allows null
            }

        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            throw new SQLException(
                    "Cannot setEqual " + prop.getName() + ": " + e.getMessage());

        }
    }

    public static boolean isCompatibleType(Object value, Class<?> type) {
        // Do object check first, then primitives
        if (value == null || type.isInstance(value)) {
            return true;

        } else if (type.equals(Integer.TYPE) && value instanceof Integer) {
            return true;

        } else if (type.equals(Long.TYPE) && value instanceof Long) {
            return true;

        } else if (type.equals(Double.TYPE) && value instanceof Double) {
            return true;

        } else if (type.equals(Float.TYPE) && value instanceof Float) {
            return true;

        } else if (type.equals(Short.TYPE) && value instanceof Short) {
            return true;

        } else if (type.equals(Byte.TYPE) && value instanceof Byte) {
            return true;

        } else if (type.equals(Character.TYPE) && value instanceof Character) {
            return true;

        } else if (type.equals(Boolean.TYPE) && value instanceof Boolean) {
            return true;

        }
        return false;

    }

    public static <T> PropertyDescriptor[] propertyDescriptors(Class<T> c)
            throws SQLException {
        // Introspector caches BeanInfo classes for better performance
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(c);

        } catch (IntrospectionException e) {
            throw new SQLException(
                    "Bean introspection failed: " + e.getMessage());
        }

        return beanInfo.getPropertyDescriptors();
    }

    public static int[] mapColumnsToProperties(ResultSetMetaData rsmd,
                                               PropertyDescriptor[] props, Map<String, String> columnToPropertyOverrides) throws SQLException {

        int cols = rsmd.getColumnCount();
        int[] columnToProperty = new int[cols + 1];
        Arrays.fill(columnToProperty, PROPERTY_NOT_FOUND);

        for (int col = 1; col <= cols; col++) {
            String columnName = rsmd.getColumnLabel(col);
            if (null == columnName || 0 == columnName.length()) {
                columnName = rsmd.getColumnName(col);
            }
            String propertyName = columnToPropertyOverrides.get(columnName);
            if (propertyName == null) {
                propertyName = columnName;
            }
            for (int i = 0; i < props.length; i++) {

                if (propertyName.equalsIgnoreCase(props[i].getName())) {
                    columnToProperty[col] = i;
                    break;
                }
            }
        }

        return columnToProperty;
    }

}
