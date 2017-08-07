package com.silentgo.utils.asm;

import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Project : SilentGo
 * Package : com.silentgo.utils.asm
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by teddyzhu on 2017/1/19.
 */
public class ParameterNameUtils {

    private static final Log LOGGER = LogFactory.get();

    private static Map<Method, String[]> parameterMap = new HashMap<>();

    // marker object for classes that do not have any debug info
    private static final Map<Member, String[]> NO_DEBUG_INFO_MAP = Collections.emptyMap();

    // the cache uses a nested index (value is a map) to keep the top level cache relatively small in size
    private static final Map<Class<?>, Map<Member, String[]>> parameterNamesCache = new ConcurrentHashMap<>(32);


    public static String[] getMethodParameterNames(final Method method) {
        return getMethodParameterNames(method.getDeclaringClass(), method);
    }

    /**
     * 获取指定类指定方法的参数名
     *
     * @param declaringClass  要获取参数名的方法所属的类
     * @param method 要获取参数名的方法
     * @return 按参数顺序排列的参数名列表，如果没有参数，则返回null
     */
    public static String[] getMethodParameterNames(Class<?> declaringClass, final Method method) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes == null || parameterTypes.length == 0) {
            return new String[0];
        }

        Map<Member, String[]> map = parameterNamesCache.get(declaringClass);
        if (map == null) {
            map = inspectClass(declaringClass);
            parameterNamesCache.put(declaringClass, map);
        }
        if (map != NO_DEBUG_INFO_MAP) {
            return map.get(method);
        }
        return null;
    }

    private static Map<Member, String[]> inspectClass(Class<?> clazz) {
        InputStream is = clazz.getResourceAsStream(ClassUtils.getClassFileName(clazz));
        if (is == null) {
            // We couldn't load the class file, which is not fatal as it
            // simply means this method of discovering parameter names won't work.
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Cannot find '.class' file for class [" + clazz +
                        "] - unable to determine constructor/method parameter names");
            }
            return NO_DEBUG_INFO_MAP;
        }
        try {
            ClassReader classReader = new ClassReader(is);
            Map<Member, String[]> map = new ConcurrentHashMap<>(32);
            classReader.accept(new ParameterNameDiscoveringVisitor(clazz, map), 0);
            return map;
        }
        catch (IOException ex) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception thrown while reading '.class' file for class [" + clazz +
                        "] - unable to determine constructor/method parameter names", ex);
            }
        }
        catch (IllegalArgumentException ex) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ASM ClassReader failed to parse class file [" + clazz +
                        "], probably due to a new Java class file version that isn't supported yet " +
                        "- unable to determine constructor/method parameter names", ex);
            }
        }
        finally {
            try {
                is.close();
            }
            catch (IOException ex) {
                // ignore
            }
        }
        return NO_DEBUG_INFO_MAP;
    }
}
