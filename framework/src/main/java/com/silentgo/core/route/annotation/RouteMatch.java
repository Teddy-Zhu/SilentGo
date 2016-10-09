package com.silentgo.core.route.annotation;

import com.silentgo.servlet.http.RequestMethod;

import java.lang.annotation.*;

/**
 * Project : silentgo
 * com.silentgo.core.route.annotation
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/30.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RouteMatch {

    RequestMethod[] method() default {};

    String[] params() default {};

    String[] pathParams() default {};

    String[] headers() default {};

    String[] consumes() default {};

    String[] produces() default {};
}
