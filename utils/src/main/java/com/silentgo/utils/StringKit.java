package com.silentgo.utils;

import java.util.Collection;
import java.util.Iterator;

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
        if (isBlank(field)) return field;
        return field.substring(0, 1).toUpperCase() + field.toLowerCase().substring(1);
    }
    public static boolean isNotBlank(String str) {
        return !isNull(str) && str.length() != 0 && "".equals(str.trim());
    }

    public static boolean isBlank(String str) {
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
    public static String join(Object[] array, String separator) {
        return array == null?null:join(array, separator, 0, array.length);
    }

    public static String join(Object[] array, String separator, int startIndex, int endIndex) {
        if(array == null) {
            return null;
        } else {
            if(separator == null) {
                separator = "";
            }

            int bufSize = endIndex - startIndex;
            if(bufSize <= 0) {
                return "";
            } else {
                bufSize *= (array[startIndex] == null?16:array[startIndex].toString().length()) + separator.length();
                StringBuilder buf = new StringBuilder(bufSize);

                for(int i = startIndex; i < endIndex; ++i) {
                    if(i > startIndex) {
                        buf.append(separator);
                    }

                    if(array[i] != null) {
                        buf.append(array[i]);
                    }
                }

                return buf.toString();
            }
        }
    }

    public static String join(Collection collection, String separator) {
        return collection == null ? null : join(collection.iterator(), separator);
    }

    public static String join(Iterator iterator, String separator) {
        if (iterator == null) {
            return null;
        } else if (!iterator.hasNext()) {
            return "";
        } else {
            Object first = iterator.next();
            if (!iterator.hasNext()) {
                return first.toString();
            } else {
                StringBuilder buf = new StringBuilder(256);
                if (first != null) {
                    buf.append(first);
                }
                while (iterator.hasNext()) {
                    if (separator != null) {
                        buf.append(separator);
                    }

                    Object obj = iterator.next();
                    if (obj != null) {
                        buf.append(obj);
                    }
                }

                return buf.toString();
            }
        }
    }
}
