package com.silentgo.core.plugin.event.annotation;

import java.lang.annotation.*;

/**
 * Project : SilentGo
 * Package : com.silentgo.core.plugin.event.annotation
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/26.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface EventListen {
    int delayTime() default 0;

    boolean async() default false;
}
