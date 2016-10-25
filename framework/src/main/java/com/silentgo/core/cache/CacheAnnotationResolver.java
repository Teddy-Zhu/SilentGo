package com.silentgo.core.cache;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.annotationintercept.IAnnotation;
import com.silentgo.core.aop.annotationintercept.annotation.CustomInterceptor;
import com.silentgo.core.aop.annotationintercept.support.AnnotationInterceptChain;
import com.silentgo.core.cache.annotation.Cache;
import com.silentgo.core.config.Const;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;
import com.silentgo.utils.StringKit;

import java.util.Collection;


/**
 * Project : parent
 * Package : com.silentgo.core.cache
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/8.
 */
@CustomInterceptor
public class CacheAnnotationResolver implements IAnnotation<Cache> {
    @Override
    public int priority() {
        return 5;
    }

    @Override
    public Object intercept(AnnotationInterceptChain chain, Response response, Request request, Cache cache) throws Throwable {
        Object returnValue;
        boolean useCache;
        Object cacheKey = null;
        SilentGo instance = SilentGo.me();
        CacheManager cacheManager = instance.getConfig().getCacheManager();
        Object[] objects = chain.getPoint().getObjects();

        cacheKey = cache.index() == -1 ? cache.key() : objects[cache.index()];
        if (cacheKey.getClass().isArray() || cacheKey instanceof Collection) {
            cacheKey = StringKit.join((String[]) cacheKey, ",");
        } else {
            cacheKey = cacheKey.toString();
        }

        cacheKey = chain.getPoint().getMethod().getName() + cacheKey;

        returnValue = cacheManager.get(cache.cacheName(), cacheKey);
        if (returnValue != null) {
            return returnValue;
        } else if (!cache.defaultValue().equals(Const.DEFAULT_NONE)) {
            cacheManager.set(cache.cacheName(), cacheKey, cache.defaultValue());
            return cache.defaultValue();
        }
        returnValue = chain.intercept();
        cacheManager.set(cache.cacheName(), cacheKey, returnValue);
        return returnValue;
    }

}
