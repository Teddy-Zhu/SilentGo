package com.silentgo.core.cache.annotation;

import com.silentgo.core.config.Const;

import java.lang.annotation.*;

/**
 * Project : parent
 * Package : com.silentgo.core.cache.annotation
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/8.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Cache {
    //cache name
    String cacheName() default Const.DEFAULT_NONE;
    //cache dynamic key
    int index() default -1;

    // cache key
    String key() default Const.DEFAULT_NONE;

    // defaultValue if null
    String defaultValue() default Const.DEFAULT_NONE;
}
