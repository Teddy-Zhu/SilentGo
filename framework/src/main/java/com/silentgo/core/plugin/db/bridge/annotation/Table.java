package com.silentgo.core.plugin.db.bridge.annotation;

import com.silentgo.core.config.Const;

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
    String value() default Const.DEFAULT_NONE;

    String[] primaryKey() default Const.DEFAULT_PY_KEY;

}
