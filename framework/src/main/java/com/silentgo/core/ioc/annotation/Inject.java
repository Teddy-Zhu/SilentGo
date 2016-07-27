package com.silentgo.core.ioc.annotation;

import com.silentgo.config.Const;

import java.lang.annotation.*;

/**
 * Project : silentgo
 * com.silentgo.core.ioc.annotation
 *
 * @author <Acc href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</Acc>
 *         <p>
 *         Created by  on 16/7/18.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Inject {
    String value() default Const.DEFAULT_NONE;
}
