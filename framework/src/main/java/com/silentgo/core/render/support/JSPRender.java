package com.silentgo.core.render.support;

import com.silentgo.core.render.SilentGoRender;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Project : silentgo
 * com.silentgo.core.render.support
 *
 * @author    <Acc href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</Acc>
 * <p>
 * Created by teddyzhu on 16/7/25.
 */
public class JSPRender implements SilentGoRender {
    String view ;

    public JSPRender(String view) {
        this.view = view;
    }

    @Override
    public void render(Response response, Request request) {
        try {
            request.getRequestDispatcher(view).forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
}
