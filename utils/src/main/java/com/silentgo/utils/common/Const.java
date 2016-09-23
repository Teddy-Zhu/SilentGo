package com.silentgo.utils.common;

import com.silentgo.utils.PropKit;

/**
 * Project : silentgo
 * com.silentgo.utils.common
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/20.
 */
public class Const {

    public static final String EmptyString = "";

    public static final String Version = "version";

    public static final String version = new PropKit("application.properties").getValue(Version);
}
