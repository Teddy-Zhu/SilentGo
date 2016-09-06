package com.silentgo.core.action;

import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

/**
 * Project : silentgo
 * com.silentgo.core.action
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by  on 16/7/18.
 */
public class ActionParam {

    private boolean isHandled;

    private Request request;
    private Response response;

    private String requestURL;


    public ActionParam(boolean isHandled, Request request, Response response, String requestURL) {
        this.isHandled = isHandled;
        this.request = request;
        this.response = response;
        this.requestURL = requestURL;
    }

    public boolean isHandled() {
        return isHandled;
    }

    public void setHandled(boolean handled) {
        isHandled = handled;
    }

    public Request getRequest() {
        return request;
    }

    public Response getResponse() {
        return response;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
