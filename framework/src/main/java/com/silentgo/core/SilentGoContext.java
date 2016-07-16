package com.silentgo.core;

import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

import javax.servlet.ServletContext;

/**
 * Created by teddyzhu on 16/7/15.
 */
public class SilentGoContext {

    /**
     * SilentGoContext object for the current thread
     */
    private static ThreadLocal<SilentGoContext> ctx = new ThreadLocal<SilentGoContext>();

    private ServletContext context;

    private Request request;

    private Response response;

    private SilentGoContext() {

    }

    private SilentGoContext(ServletContext context, Request request, Response response) {
        this.context = context;
        this.request = request;
        this.response = response;
    }

    public SilentGoContext(ServletContext context) {
        this.context = context;
    }

    public static void setContext(ServletContext context) {
        ctx.set(new SilentGoContext(context));
    }

    public static void setContext(ServletContext context, Request request, Response response) {
        ctx.set(new SilentGoContext(context, request, response));
    }

}
