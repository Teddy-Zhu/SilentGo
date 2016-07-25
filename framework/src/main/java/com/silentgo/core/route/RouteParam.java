package com.silentgo.core.route;

import java.lang.annotation.Annotation;
import java.util.HashMap;

/**
 * Project : silentgo
 * com.silentgo.core.route
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by  on 16/7/18.
 */
public class RouteParam {

    public String name;

    public HashMap<Class<? extends Annotation>, Annotation> annotations;

}
