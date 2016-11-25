package com.silentgo.core.db.intercept;

import com.silentgo.core.config.Const;
import com.silentgo.core.db.propagation.Propagation;

import java.lang.annotation.*;
import java.sql.Connection;

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

    String value() default Const.DEFAULT_NONE;

    int transcationLevel() default Connection.TRANSACTION_REPEATABLE_READ;

    Class<? extends Exception>[] rollback() default {Exception.class};

    Propagation propagation() default Propagation.PROPAGATION_REQUIRED;
}
