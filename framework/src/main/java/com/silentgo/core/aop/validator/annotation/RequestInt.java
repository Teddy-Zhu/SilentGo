package com.silentgo.core.aop.validator.annotation;

import com.silentgo.core.config.Const;

import java.lang.annotation.*;

/**
 * Project : SilentGo
 * Package : com.silentgo.core.aop.validator.annotation
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/13.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface RequestInt {
    boolean required() default true;

    int[] range() default {0, Integer.MAX_VALUE};

    String defaultValue() default Const.DEFAULT_NONE;
}
