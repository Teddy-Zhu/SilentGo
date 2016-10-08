package com.silentgo.core.action;

import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

import javax.servlet.FilterChain;

/**
 * Project : silentgo
 * com.silentgo.core.action
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by  on 16/7/18.
 */
public class ActionParam {

    private Request request;
    private Response response;

    private String requestURL;

    private FilterChain filterChain;

    public ActionParam(Request request, Response response, String requestURL, FilterChain filterChain) {
        this.request = request;
        this.response = response;
        this.requestURL = requestURL;
        this.filterChain = filterChain;
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

    public void setResponse(Response response) {
        this.response = response;
    }

    public FilterChain getFilterChain() {
        return filterChain;
    }

}
