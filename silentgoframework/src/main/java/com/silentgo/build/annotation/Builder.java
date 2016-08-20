package com.silentgo.build.annotation;

import java.lang.annotation.*;

/**
 * Project : silentgo
 * com.silentgo.build.annotation
 *
 * @author    <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by teddyzhu on 16/8/16.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Builder {
}
