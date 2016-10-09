package com.silentgo.core.route.support.requestdispatch.annotation;

import java.lang.annotation.*;

/**
 * Project : parent
 * Package : com.silentgo.core.route.support.requestdispatch.annotation
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/9.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RequestDispatch {
}
