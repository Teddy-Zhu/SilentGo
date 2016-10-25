package com.silentgo.core.db.annotation;

import com.silentgo.core.config.Const;

import java.lang.annotation.*;

/**
 * Project : SilentGo
 * Package : com.silentgo.core.db.annotation
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/24.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Query {
    public String[] value();
}
