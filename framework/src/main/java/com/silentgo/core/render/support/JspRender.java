package com.silentgo.core.render.support;

import com.silentgo.core.action.ActionParam;
import com.silentgo.core.exception.AppRenderException;
import com.silentgo.core.render.Render;
import com.silentgo.core.route.Route;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Project : silentgo
 * com.silentgo.core.render.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/25.
 */
public class JspRender implements Render {
    private static String baseView;

    public JspRender(String view) {
        baseView = view;
    }

    @Override
    public void render(Response response, Request request, Object retVal) throws AppRenderException {
        try {
            String view = baseView + retVal;
            request.getRequestDispatcher(view).forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
}
