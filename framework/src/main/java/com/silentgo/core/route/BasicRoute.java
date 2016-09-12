package com.silentgo.core.route;

import com.silentgo.core.aop.MethodAdviser;

import java.util.regex.Pattern;

/**
 * Project : silentgo
 * com.silentgo.core.route
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by  on 16/7/18.
 */
public class BasicRoute {

    private String path;

    private MethodAdviser adviser;

    public String getPath() {
        return path;
    }

    public MethodAdviser getAdviser() {
        return adviser;
    }

    public BasicRoute(String path, MethodAdviser adviser) {
        this.path = path;
        this.adviser = adviser;
    }

    public BasicRoute() {
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setAdviser(MethodAdviser adviser) {
        this.adviser = adviser;
    }
}
