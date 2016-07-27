package com.silentgo.kit.typeconvert.support;

import com.silentgo.kit.ClassKit;
import com.silentgo.kit.typeconvert.ITypeConvertor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.kit.typeconvert.support
 *
 * @author <Acc href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</Acc>
 *         <p>
 *         Created by teddyzhu on 16/7/26.
 */
public class TypeConvert {

    private static final Class<?>[] type = {
            Boolean.class, Byte.class, Character.class,
            Double.class, Integer.class, Long.class,
            Float.class, Short.class};

    private static final Class<?>[] primitiveType = {
            boolean.class, byte.class, char.class,
            double.class, int.class, long.class,
            float.class, short.class};

    private static final List<Class<?>> typeList = new ArrayList<>();
    private static final Map<Class<?>, Class<?>> cachedMap = new HashMap<>();

    static {
        for (int i = 0, len = type.length; i < len; i++) {
            cachedMap.put(primitiveType[i], type[i]);
            typeList.add(type[i]);
        }
    }

    public static Class<?> getClass(Object source) {
        return getClass(source.getClass());
    }

    public static Class<?> getClass(Class<?> clz) {
        Class<?> tclz = cachedMap.get(clz);
        return tclz == null ? clz : tclz;
    }

    public static boolean isBaseType(Class<?> clz) {
        return typeList.contains(clz);
    }

}
