package com.silentgo.core.aop.validator.annotation;

import java.lang.annotation.*;

/**
 * Project : silentgo
 * com.silentgo.core.aop.validator.annotation
 *
 * @author    <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by  on 16/7/18.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Validator {
}
