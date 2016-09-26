package com.silentgo.core.build;

import java.lang.annotation.*;

/**
 * Project : silentgo
 * com.silentgo.core.build.annotation
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/26.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Factory {
}
