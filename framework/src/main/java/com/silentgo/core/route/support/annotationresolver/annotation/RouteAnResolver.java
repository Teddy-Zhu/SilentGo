package com.silentgo.core.route.support.annotationresolver.annotation;

import java.lang.annotation.*;

/**
 * Project : silentgo
 * com.silentgo.core.route.support.annotationresolver.annotation
 *
 * @author    <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by teddyzhu on 16/8/30.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RouteAnResolver {
}
