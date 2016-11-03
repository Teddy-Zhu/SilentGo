package com.silentgo.core.exception.support;

import com.silentgo.core.render.RenderModel;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

/**
 * Project : silentgo
 * com.silentgo.core.exception.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/29.
 */
public interface IExceptionHandler {

    public RenderModel resolve(Response response, Request request, Exception ex);
}
