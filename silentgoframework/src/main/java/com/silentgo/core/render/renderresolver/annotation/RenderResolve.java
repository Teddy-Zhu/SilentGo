package com.silentgo.core.render.renderresolver.annotation;

import java.lang.annotation.*;

/**
 * Project : silentgo
 * com.silentgo.core.render.renderresolver.annotation
 *
 * @author    <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by teddyzhu on 16/8/30.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RenderResolve {
}
