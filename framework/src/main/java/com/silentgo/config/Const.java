package com.silentgo.config;

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
        Const.BasePackages.add("com.silentgo");
    }

    public static final String Encoding = "UTF-8";

    public static final String Slash = "/";

    public static final String DEFAULT_NONE = "\n\t\t\n\t\t\n\uE000\uE001\uE002\n\t\t\t\t\n";

    @SuppressWarnings("unchecked")
    public static final ArrayList BasePackages = new ArrayList<>();

    public static final ArrayList EmptyArray = new ArrayList();
}
