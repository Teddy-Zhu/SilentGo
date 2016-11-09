package com.silentgo.utils;

import com.silentgo.utils.convertor.*;
import com.silentgo.utils.inter.ITypeConvertor;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Project : SilentGo
 * Package : com.silentgo.utils
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/11/9.
 */
public class ConvertKit {
    private static Map<Class<?>, Map<Class<?>, ITypeConvertor>> convertMap = new HashMap<>();

    private static ITypeConvertor commonConvertor = new CommonConvertor();

    static {
        Map<Class<?>, ITypeConvertor> t = new HashMap<>();
        t.put(String.class, new StringToStringConvertor());
        t.put(Long.class, new StringToLongConvertor());
        t.put(Integer.class, new StringToIntegerConvertor());
        t.put(Date.class, new StringToDateConvertor());
        t.put(Boolean.class, new StringToBooleanConvertor());
        convertMap.put(String.class, t);
    }


    public static ITypeConvertor getTypeConvert(Class<?> source, Class<?> target, ITypeConvertor defaultVal) {
        if (target.isPrimitive()) {
            target = TypeConvertKit.getConvertType(target);
        }

        Map<Class<?>, ITypeConvertor> convertorMap = convertMap.get(source);
        return convertorMap == null ? defaultVal : convertorMap.getOrDefault(target, defaultVal);
    }

    public static ITypeConvertor getTypeConvert(Class<?> source, Class<?> target) {
        return getTypeConvert(source, target, commonConvertor);
    }

    public boolean addConvert(ITypeConvertor convert) {
        Class<?> convertClass = convert.getClass();
        Type[] types = ClassKit.getGenericClass(convertClass);

        Class<?> source = (Class<?>) types[0];

        Class<?> target = (Class<?>) types[1];

        Map converts = convertMap.get(source);
        if (converts == null) {
            converts = new HashMap<Class<?>, ITypeConvertor>() {{
                put(target, convert);
            }};
            convertMap.put(source, converts);
        } else {
            if (!converts.containsKey(target))
                converts.put(target, convert);
        }
        return true;
    }

    public boolean addConvert(Class<?> convert) {
        try {
            return addConvert((ITypeConvertor) convert.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }


}
