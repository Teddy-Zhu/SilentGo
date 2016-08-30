package com.silentgo.core.exception.support;

import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.aop.MethodParam;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

/**
 * Project : silentgo
 * com.silentgo.core.exception.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/30.
 */
public class ExceptionKit {

    public static Object[] getArgs(MethodAdviser methodAdviser, Exception e, Response response, Request request) {
        Object[] ret = new Object[methodAdviser.getParams().length];

        for (int i = 0, len = methodAdviser.getParams().length; i < methodAdviser.getParams().length; i++) {

            Class<?> type = methodAdviser.getParams()[i].getType();
            if (type.equals(Response.class)) {
                ret[i] = response;
                continue;
            }
            if (type.equals(Request.class)) {
                ret[i] = request;
                continue;
            }
            if (Exception.class.isAssignableFrom(type)) {
                ret[i] = e;
            }
        }
        return ret;
    }

}
