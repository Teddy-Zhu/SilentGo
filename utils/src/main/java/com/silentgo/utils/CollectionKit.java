package com.silentgo.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.kit
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/27.
 */
public class CollectionKit {

    public static boolean isEmpty(Object[] collection) {
        return collection == null || collection.length == 0;
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static <K, V> List<V> ListMapAdd(Map<K, List<V>> map, K key, V value) {
        if (key == null) return new ArrayList<>();
        List<V> list = map.get(key);
        if (list == null) {
            list = new ArrayList<>();
            map.put(key, list);
        }
        list.add(value);
        return list;
    }

    public static <K, V> List<V> ListMapAdd(Map<K, List<V>> map, K key, List<V> value) {
        if (key == null) return new ArrayList<>();
        List<V> list = map.get(key);
        if (list == null) {
            list = new ArrayList<>();
            map.put(key, list);
        }
        list.addAll(value);
        return list;
    }

    public static <K, V> boolean MapAdd(Map<K, V> map, K key, V value, boolean allowValueNull) {
        if (key == null || (!allowValueNull && value == null)) return false;
        map.put(key, value);
        return true;
    }

    public static <K, V> boolean MapAdd(Map<K, V> map, K key, V value) {
        return MapAdd(map, key, value, false);
    }

    public static <T> boolean ListAdd(Collection<T> list, T source) {
        return ListAdd(list, source, false);
    }

    public static <T> boolean ListAdd(Collection<T> list, T source, boolean allowDuplicate) {
        if (source == null) return false;
        if (allowDuplicate)
            return list.add(source);
        else {
            if (!list.contains(source))
                return list.add(source);
        }
        return false;
    }

    public static <T> boolean ListAdd(Collection<T> list, Collection<T> source) {
        return ListAdd(list, source, false);
    }

    public static <T> boolean ListAdd(Collection<T> list, Collection<T> source, boolean allowDuplicate) {
        if (source == null) return false;
        if (allowDuplicate)
            return list.addAll(source);
        else {
            source.forEach(s -> ListAdd(list, s, false));
        }
        return false;
    }
}
