package com.silentgo.core.exception.annotaion;

import java.lang.annotation.*;

/**
 * Project : silentgo
 * com.silentgo.core.exception.annotaion
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/29.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface ExceptionHandler {
}
