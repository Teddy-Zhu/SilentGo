package com.silentgo.kit;

/**
 * Project : silentgo
 * com.silentgo.kit
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by  on 16/7/15.
 */
public class StringKit {

    public static boolean isNullOrEmpty(String str) {
        return str == null || "".equals(str.trim());
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
