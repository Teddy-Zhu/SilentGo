package com.silentgo.core.cache;

import java.util.List;

/**
 * Project : parent
 * Package : com.silentgo.core.cache
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/8.
 */
public interface CacheManager {

    /**
     * @param name cache name/group
     * @param key  cache key
     * @return
     */
    Object get(Object name, Object key);

    void set(Object name, Object key, Object value);

    boolean evict(Object name, Object key);

    void evict(Object name);

    List<Object> getKeys(Object name);

    List<Object> getNames();
}
