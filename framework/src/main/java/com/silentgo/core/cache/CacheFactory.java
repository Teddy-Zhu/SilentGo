package com.silentgo.core.cache;

import com.silentgo.core.SilentGo;
import com.silentgo.core.build.Factory;
import com.silentgo.core.exception.AppBuildException;
import com.silentgo.core.exception.AppReleaseException;
import com.silentgo.core.support.BaseFactory;

/**
 * Project : parent
 * Package : com.silentgo.core.cache
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/8.
 */
@Factory
public class CacheFactory extends BaseFactory {
    @Override
    public boolean initialize(SilentGo me) throws AppBuildException {
        Class<? extends CacheManager> cacheClz = me.getConfig().getCacheClz();
        if (cacheClz != null) {
            try {
                me.getConfig().getCacheManagerMap().put(cacheClz, cacheClz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public boolean destroy(SilentGo me) throws AppReleaseException {
        return false;
    }
}
