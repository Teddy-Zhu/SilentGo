package com.silentgo.utils;

import java.sql.SQLXML;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Project : silentgo
 * com.silentgo.kit.typeconvert.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/26.
 */
public class TypeConvertKit {

    private static final ArrayList<Class<?>> type = new ArrayList<Class<?>>() {{
        add(Boolean.class);
        add(Byte.class);
        add(Character.class);
        add(Double.class);
        add(Integer.class);
        add(Long.class);
        add(Float.class);
        add(Short.class);
        add(String.class);
    }};


    private static final ArrayList primitiveType = new ArrayList() {{
        add(boolean.class);
        add(byte.class);
        add(char.class);
        add(double.class);
        add(int.class);
        add(long.class);
        add(float.class);
        add(short.class);
    }};

    private static final ArrayList sqlType = new ArrayList() {{
        add(Timestamp.class);
        add(SQLXML.class);
    }};

    public static boolean isSqlBaseType(Class<?> clz) {
        return type.contains(clz) || primitiveType.contains(clz) || sqlType.contains(clz);
    }


    public static Class<?> getConvertType(Class<?> clz) {
        return type.get(primitiveType.indexOf(clz));
    }

    public static boolean isBaseType(Class<?> clz) {
        return type.contains(clz) || primitiveType.contains(clz);
    }
}
