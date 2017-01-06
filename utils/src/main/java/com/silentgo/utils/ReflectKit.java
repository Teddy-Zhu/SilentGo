package com.silentgo.utils;

import com.silentgo.utils.reflect.SGClass;
import com.silentgo.utils.reflect.SGClassParseKit;

import java.util.HashMap;
import java.util.Map;

/**
 * Project : SilentGo
 * Package : com.silentgo.utils
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2017/1/5.
 */
public class ReflectKit {

    private static final Map<Class<?>, SGClass> cachedSGClass = new HashMap<>();

    public static SGClass getSGClass(Class<?> clz) {
        Assert.isNotNull(clz, "class must be not null");
        SGClass sgClass = cachedSGClass.get(clz);
        if (sgClass != null) return sgClass;
        sgClass = SGClassParseKit.parse(clz);
        cachedSGClass.put(clz, sgClass);
        return sgClass;
    }
}
