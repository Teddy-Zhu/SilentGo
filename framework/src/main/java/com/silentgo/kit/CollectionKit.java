package com.silentgo.kit;

import java.util.*;

/**
 * Project : silentgo
 * com.silentgo.kit
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/27.
 */
public class CollectionKit {

    public static <K, V> List<V> ListMapAdd(Map<K, List<V>> map, K key, V value) {
        List<V> list = map.get(key);
        if (list == null) {
            list = new ArrayList<V>();
            map.put(key, list);
        }
        list.add(value);
        return list;
    }

    public static <K, V> List<V> ListMapAdd(Map<K, List<V>> map, K key, List<V> value) {
        List<V> list = map.get(key);
        if (list == null) {
            list = new ArrayList<>();
            map.put(key, list);
        }
        list.addAll(value);
        return list;
    }

    public static <K, V> boolean MapAdd(Map<K, V> map, K key, V value) {
        if (map.containsKey(key)) {
            map.put(key, value);
            return true;
        }
        return false;
    }

}
