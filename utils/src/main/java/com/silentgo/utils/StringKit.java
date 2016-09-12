package com.silentgo.utils;

/**
 * Project : silentgo
 * com.silentgo.kit
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by  on 16/7/15.
 */
public class StringKit {

    public static String FirstToUpper(String field) {
        if (isNullOrEmpty(field)) return field;
        return field.substring(0, 1).toUpperCase() + field.toLowerCase().substring(1);
    }

    public static boolean isNullOrEmpty(String str) {
        return isNull(str) || str.length() == 0 || "".equals(str.trim());
    }

    public static boolean isNull(String str) {
        return str == null;
    }

    public static String getLeft(String source, String split) {
        return source.substring(0, source.indexOf(split));
    }

    public static String getRight(String source, String split) {
        return source.substring(source.indexOf(split) + split.length(), source.length());
    }

    public static boolean equals(String source, String target, boolean caseSensitive) {
        return !(source == null || target == null) && (caseSensitive ? source.equals(target) : source.toLowerCase().equals(target.toLowerCase()));
    }
}
