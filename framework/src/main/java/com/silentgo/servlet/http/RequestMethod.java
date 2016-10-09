package com.silentgo.servlet.http;

/**
 * Created by teddyzhu on 16/7/15.
 */
public enum RequestMethod {
    GET("get"), POST("post"), PUT("put"), DELETE("delete"), HEAD("head"), TRACE("trace"), CONNECT("connect"), OPTIONS("options");

    String method;

    RequestMethod(String string) {
        this.method = string;
    }

    public static RequestMethod prase(String method) {
        for (RequestMethod requestMethod : RequestMethod.values()) {
            if (requestMethod.method.equalsIgnoreCase(method)) {
                return requestMethod;
            }
        }
        return RequestMethod.GET;
    }

    public boolean equals(String method) {
        return this.method.equals(method);
    }
}
