package com.silentgo.core.config;

import com.silentgo.core.aop.annotation.Intercept;
import com.silentgo.core.aop.annotationintercept.annotation.CustomInterceptor;
import com.silentgo.core.ioc.annotation.Component;
import com.silentgo.core.ioc.annotation.Service;
import com.silentgo.core.route.annotation.Controller;

import java.util.ArrayList;

/**
 * Project : silentgo
 * com.silentgo.core.config
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by  on 16/7/18.
 */
public final class Const {

    private Const() {
    }

    public static final Integer DefaultMax = Integer.MAX_VALUE - 1;

    public static final String Encoding = "UTF-8";

    public static final String Slash = "/";

    public static final String DEFAULT_NONE = "";

    @SuppressWarnings("unchecked")
    public static final ArrayList BasePackages = new ArrayList() {{
        add("com.silentgo");
    }};

    public static final ArrayList EmptyArray = new ArrayList();

    public static final String EmptyString = "";

    public static final ArrayList KeyAnnotations = new ArrayList() {{
        add(Controller.class);
        add(Component.class);
        add(Service.class);
        add(CustomInterceptor.class);
        add(Intercept.class);
    }};


    public static final String Version = "0.0.10";

    public static final String configName = "application.properties";

    public static final String ApplicationName = "framework";

    public static final String ApplicationUtils = "utils";


    public static final String BaseView = "/WEB-INF/";

    public static final String FileUploadConfig = "FileUpload";


    public static final String DEFAULT_PY_KEY = "id";

}
