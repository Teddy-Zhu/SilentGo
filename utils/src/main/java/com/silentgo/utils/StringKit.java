package com.silentgo.utils;

import com.alibaba.fastjson.JSON;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.kit
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by  on 16/7/15.
 */
public class StringKit {


    public static String firstToUpper(String field) {
        if (isBlank(field)) return field;
        char first = field.charAt(0);
        return Character.isUpperCase(first) ? field : (Character.toUpperCase(first) + field.substring(1));
    }

    public static String firstToLower(String field) {
        if (isBlank(field)) return field;
        char first = field.charAt(0);
        return Character.isLowerCase(first) ? field : (Character.toLowerCase(first) + field.substring(1));
    }

    public static boolean isNotBlank(String str) {
        return !isNull(str) && str.trim().length() != 0;
    }

    public static boolean isBlank(String str) {
        return isNull(str) || str.trim().length() == 0;
    }

    public static boolean isNull(String str) {
        return str == null;
    }

    public static String[] getLeftRight(String source, String split) {
        String[] ret = new String[]{"", ""};
        int index = source.indexOf(split);
        if (index == -1) return ret;
        ret[0] = source.substring(0, index);
        ret[1] = source.substring(index + split.length());
        return ret;
    }

    public static String getLeft(String source, String split) {
        return source.substring(0, source.indexOf(split));
    }

    public static String getRight(String source, String split) {
        return source.substring(source.indexOf(split) + split.length());
    }

    public static boolean equals(String source, String target, boolean caseSensitive) {
        return !(source == null || target == null) && (caseSensitive ? source.equals(target) : source.toLowerCase().equals(target.toLowerCase()));
    }

    public static String join(Object[] array, String separator) {
        return array == null ? null : join(array, separator, 0, array.length);
    }

    public static String join(Object[] array, String separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        } else {
            if (separator == null) {
                separator = "";
            }

            int bufSize = endIndex - startIndex;
            if (bufSize <= 0) {
                return "";
            } else {
                bufSize *= (array[startIndex] == null ? 16 : array[startIndex].toString().length()) + separator.length();
                StringBuilder buf = new StringBuilder(bufSize);

                for (int i = startIndex; i < endIndex; ++i) {
                    if (i > startIndex) {
                        buf.append(separator);
                    }

                    if (array[i] != null) {
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

    public static String toString(Object object) {
        if (object instanceof String) {
            return (String) object;
        }
        if (object.getClass().isPrimitive()) {
            return String.valueOf(object);
        }
        return object.toString();
    }

    public static String format(String template, Object... values) {
        if (CollectionKit.isEmpty(values) || isBlank(template)) {
            return template;
        }

        final StringBuilder sb = new StringBuilder();
        final int length = template.length();

        int valueIndex = 0;
        char currentChar;
        for (int i = 0; i < length; i++) {
            if (valueIndex >= values.length) {
                sb.append(template.substring(i));
                break;
            }

            currentChar = template.charAt(i);
            if (currentChar == '{') {
                final char nextChar = template.charAt(++i);
                if (nextChar == '}') {
                    sb.append(toString(values[valueIndex++]));
                } else {
                    sb.append('{').append(nextChar);
                }
            } else {
                sb.append(currentChar);
            }

        }

        return sb.toString();
    }

    public static String format(String template, Map<String, ?> map) {
        if (null == map || map.isEmpty()) {
            return template;
        }

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            template = template.replace("{" + entry.getKey() + "}", entry.getValue().toString());
        }
        return template;
    }

}
