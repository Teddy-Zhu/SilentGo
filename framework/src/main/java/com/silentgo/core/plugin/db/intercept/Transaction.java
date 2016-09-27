package com.silentgo.core.plugin.db.intercept;

import java.lang.annotation.*;

/**
 * Project : parent
 * Package : com.silentgo.core.aop.annotationintercept.innerimpl.annotation
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/27.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Transaction {

    Class<? extends Exception>[] rollback() default {Exception.class};
}
