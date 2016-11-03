package com.silentgo.servlet;

import com.silentgo.core.action.ActionParam;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

import java.util.HashMap;

/**
 * Project : silentgo
 * com.silentgo.kit
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/20.
 */
public class SilentGoContext {

    ActionParam actionParam;


    //origin
    String hashString;
    private Object hashObject;

    private Object jsonObject;

    private String jsonString;

    public SilentGoContext(ActionParam param) {
        this.actionParam = param;
    }

    public String getHashString() {
        return hashString;
    }

    public void setHashString(String hashString) {
        this.hashString = hashString;
    }

    public Response getResponse() {
        return actionParam.getResponse();
    }

    public void setResponse(Response response) {
        this.actionParam.setResponse(response);
    }

    public Request getRequest() {
        return actionParam.getRequest();
    }

    public void setRequest(Request request) {
        actionParam.setRequest(request);
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }

    public ActionParam getActionParam() {
        return actionParam;
    }

    public Object getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(Object jsonObject) {
        this.jsonObject = jsonObject;
    }

    public Object getHashObject() {
        return hashObject;
    }

    public void setHashObject(Object hashObject) {
        this.hashObject = hashObject;
    }
}
