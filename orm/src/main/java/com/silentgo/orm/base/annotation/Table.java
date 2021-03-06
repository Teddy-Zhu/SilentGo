package com.silentgo.orm.base.annotation;


import com.silentgo.orm.common.Const;

import java.lang.annotation.*;

/**
 * Project : silentgo
 * com.silentgo.core.plugin.db.bridge.annotation
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/22.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Table {
    String value() default Const.EmptyString;

    String[] primaryKey() default Const.DEFAULT_PY_KEY;

}
