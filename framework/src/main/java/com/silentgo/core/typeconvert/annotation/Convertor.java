package com.silentgo.core.typeconvert.annotation;

import java.lang.annotation.*;

/**
 * Project : silentgo
 * com.silentgo.kit.typeconvert
 *
 * @author    <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by teddyzhu on 16/8/20.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Convertor {
}
