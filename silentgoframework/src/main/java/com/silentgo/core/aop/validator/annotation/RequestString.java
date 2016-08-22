package com.silentgo.core.aop.validator.annotation;

import com.silentgo.core.config.Const;

import java.lang.annotation.*;

/**
 * Project : silentgo
 * com.silentgo.core.aop.validator.annotation
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by  on 16/7/18.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface RequestString {
    boolean required() default false;

    int[] range() default {0, Integer.MAX_VALUE};

    String defaultValue() default Const.DEFAULT_NONE;


}
