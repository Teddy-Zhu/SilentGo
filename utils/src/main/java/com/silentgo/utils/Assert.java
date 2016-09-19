package com.silentgo.utils;

/**
 * Project : silentgo
 * com.silentgo.utils
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/13.
 */
public class Assert {

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

    public static void isBlank(String string, String message) {
        if (!StringKit.isNullOrEmpty(string)) {
            throw new RuntimeException(message);
        }
    }

    public static void isNotBlank(String string, String message) {
        if (StringKit.isNullOrEmpty(string)) {
            throw new RuntimeException(message);
        }
    }
}
