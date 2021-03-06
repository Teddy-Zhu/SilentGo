package com.silentgo.core.render;

import com.silentgo.core.exception.AppRenderException;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

/**
 * Project : silentgo
 * com.silentgo.core.render
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/25.
 */
public interface Render {

    public void render(Response response, Request request, Object retVal) throws AppRenderException;
}
