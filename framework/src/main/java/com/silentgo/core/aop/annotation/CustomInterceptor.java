package com.silentgo.core.aop.annotation;

import java.lang.annotation.*;

/**
 * Project : silentgo
 * com.silentgo.core.aop.annotation
 *
 * @author    <Acc href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</Acc>
 * <p>
 * Created by teddyzhu on 16/7/20.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface CustomInterceptor {
}
