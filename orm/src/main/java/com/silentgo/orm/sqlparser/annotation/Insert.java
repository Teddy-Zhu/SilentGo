package com.silentgo.orm.sqlparser.annotation;

import com.silentgo.orm.common.Const;

import java.lang.annotation.*;

/**
 * Project : parent
 * Package : com.silentgo.orm.sqlparser.annotation
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/29.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Insert {
    public String value() default Const.EmptyString;
}
