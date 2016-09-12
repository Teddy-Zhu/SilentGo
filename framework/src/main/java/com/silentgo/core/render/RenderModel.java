package com.silentgo.core.render;

import com.silentgo.core.config.Const;
import com.silentgo.core.exception.AppRenderException;
import com.silentgo.core.render.support.JspRender;
import com.silentgo.core.route.Route;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

/**
 * Project : silentgo
 * com.silentgo.core.render
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/29.
 */
public class RenderModel {

    private Render render = new JspRender(Const.BaseView);

    private Object result = null;

    private Request request;
    private Response response;

    public RenderModel(Render render, Object result, Request request, Response response) {
        this.render = render;
        this.result = result;
        this.request = request;
        this.response = response;
    }

    public void render() throws AppRenderException {
        render.render(response, request, result);
    }

    public Render getRender() {
        return render;
    }

    public void setRender(Render render) {
        this.render = render;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }


}
