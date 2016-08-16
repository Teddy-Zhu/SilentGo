package com.silentgo.config;

import com.silentgo.core.aop.annotation.Around;
import com.silentgo.core.aop.annotation.Intercept;
import com.silentgo.core.aop.annotationintercept.annotation.CustomInterceptor;
import com.silentgo.core.ioc.annotation.Component;
import com.silentgo.core.ioc.annotation.Service;
import com.silentgo.core.route.annotation.Controller;

import java.util.ArrayList;

/**
 * Project : silentgo
 * com.silentgo.config
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by  on 16/7/18.
 */
public class Const {

    static {
        //noinspection unchecked
        Const.BasePackages.add(Const.BasePackageString);
    }

    public static final Integer DefaultMax = Integer.MAX_VALUE - 1;

    public static final String BasePackageString = "com.silentgo";

    public static final String Encoding = "UTF-8";

    public static final String Slash = "/";

    public static final String DEFAULT_NONE = "\n\t\t\n\t\t\n\uE000\uE001\uE002\n\t\t\t\t\n";

    @SuppressWarnings("unchecked")
    public static final ArrayList BasePackages = new ArrayList<>();

    public static final ArrayList EmptyArray = new ArrayList();

    public static final String EmptyString = "";

    public static final ArrayList KeyAnnotations = new ArrayList() {{
        add(Controller.class);
        add(Component.class);
        add(Service.class);
        add(CustomInterceptor.class);
        add(Intercept.class);
    }};
}
