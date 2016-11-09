package com.silentgo.core.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.Configuration;

import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Project : parent
 * Package : com.silentgo.core.cache
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/8.
 */
public class EhCache implements CacheManager {


    private net.sf.ehcache.CacheManager cacheManager;
    private volatile Object locker = new Object();
    private static final Logger log = LoggerFactory.getLogger(EhCache.class);

    public net.sf.ehcache.CacheManager getCacheManager() {
        return cacheManager;
    }

    public EhCache() {
        this.cacheManager = net.sf.ehcache.CacheManager.create();
    }

    public EhCache(net.sf.ehcache.CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public EhCache(String configurationFileName) {
        this.cacheManager = net.sf.ehcache.CacheManager.create(configurationFileName);
    }

    public EhCache(URL configurationFileURL) {
        this.cacheManager = net.sf.ehcache.CacheManager.create(configurationFileURL);
    }

    public EhCache(InputStream inputStream) {
        this.cacheManager = net.sf.ehcache.CacheManager.create(inputStream);
    }

    public EhCache(Configuration configuration) {
        this.cacheManager = net.sf.ehcache.CacheManager.create(configuration);
    }

    Cache getOrAddCache(String name) {
        String cacheName = name.toString();
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            synchronized (locker) {
                cache = cacheManager.getCache(cacheName);
                if (cache == null) {
                    log.warn("Could not find cache config [" + cacheName + "], using default.");
                    cacheManager.addCacheIfAbsent(cacheName);
                    cache = cacheManager.getCache(cacheName);
                    log.debug("Cache [" + cacheName + "] started.");
                }
            }
        }
        return cache;
    }

    @Override
    public Object get(Object name, Object key) {
        Element element = getOrAddCache(name.toString()).get(key);
        return element == null ? null : element.getObjectValue();
    }

    @Override
    public void set(Object name, Object key, Object value) {
        getOrAddCache(name.toString()).put(new Element(key, value));
    }

    @Override
    public boolean evict(Object name, Object key) {
        return getOrAddCache(name.toString()).remove(key);
    }

    @Override
    public void evict(Object name) {
        cacheManager.removeCache(name.toString());
    }

    @Override
    public List<Object> getKeys(Object name) {
        return getOrAddCache(name.toString()).getKeys();
    }

    @Override
    public List<Object> getNames() {
        return Arrays.asList(cacheManager.getCacheNames());
    }
}
