package com.silentgo.orm.sqlparser.annotation;

import com.silentgo.orm.common.Const;

import java.lang.annotation.*;

/**
 * Project : SilentGo
 * Package : com.silentgo.orm.sqlparser.annotation
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/11/12.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ColumnIgnore {
    public String[] value();
}
