package com.silentgo.core.db.annotation;

import com.silentgo.core.config.Const;

import java.lang.annotation.*;

/**
 * Project : parent
 * Package : com.silentgo.core.db.annotation
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/29.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Delete {
    public String value() default Const.DEFAULT_NONE;
}
