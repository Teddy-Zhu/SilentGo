package com.silentgo.core.ioc.annotation;

import java.lang.annotation.*;

/**
 * Project : SilentGo
 * Package : com.silentgo.core.ioc.annotation
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/14.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.LOCAL_VARIABLE})
public @interface Lazy {
}
