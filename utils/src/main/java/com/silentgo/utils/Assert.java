package com.silentgo.utils;

import java.util.Collection;

/**
 * Project : silentgo
 * com.silentgo.utils
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by teddyzhu on 16/9/13.
 */
public class Assert {

    public static void hasLength(String text, String message) {
        if (text == null || text.length() == 0) {
            throw new RuntimeException(message);
        }
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new RuntimeException(message);
        }
    }

    public static void isTrue(boolean result, String message) {
        if (!result) {
            throw new RuntimeException(message);
        }
    }

    public static void isNotTrue(boolean result, String message) {
        if (result) {
            throw new RuntimeException(message);
        }
    }

    public static void notEmpty(Object[] array, String message) {
        if (array == null || array.length == 0) {
            throw new RuntimeException(message);
        }
    }

    public static void isBlank(String string, String message) {
        if (!StringKit.isBlank(string)) {
            throw new RuntimeException(message);
        }
    }

    public static void isNotBlank(String string, String message) {
        if (StringKit.isBlank(string)) {
            throw new RuntimeException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object != null) {
            throw new NullPointerException(message);
        }
    }

    public static void isNull(Object object) {
        if (object != null) {
            throw new NullPointerException();
        }
    }

    public static void isNotNull(Object object) {
        if (object == null) {
            throw new NullPointerException();
        }
    }

    public static void isNotNull(Object object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
    }

    public static void isNotEmpty(Collection<?> collection, String message) {
        if (collection == null || collection.size() == 0) {
            throw new RuntimeException(message);
        }
    }
}
