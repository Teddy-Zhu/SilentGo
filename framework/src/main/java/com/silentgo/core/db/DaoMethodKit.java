package com.silentgo.core.db;

import com.silentgo.utils.CollectionKit;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Project : parent
 * Package : com.silentgo.core.db
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/6.
 */
public class DaoMethodKit {
    private static final Map<Method, DaoMethod> cachedResult = new ConcurrentHashMap<>();

    public static void addDaoMethod(Method method, DaoMethod daoMethod) {
        CollectionKit.MapAdd(cachedResult, method, daoMethod);
    }

    public static DaoMethod getDaoMethod(Method method) {
        return cachedResult.get(method);
    }
}
