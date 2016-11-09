package com.silentgo.orm.base.annotation;

import com.silentgo.orm.common.Const;

import java.lang.annotation.*;

/**
 * Project : SilentGo
 * Package : com.silentgo.orm.base.annotation
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/24.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Column {
    String value() default Const.EmptyString;

    boolean def() default false;

    boolean aic() default false;

    boolean nullable() default false;
}