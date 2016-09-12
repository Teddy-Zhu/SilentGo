package com.silentgo.servlet;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

/**
 * Project : silentgo
 * com.silentgo.kit
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/20.
 */
public class SilentGoContext {

    Response response;

    Request request;


    //origin
    String hashString;

    JSONObject jsonObject;


    //JSON
    private JSONObject parameterJson;
    private JSONArray parameterJsonArray;

    private String jsonString;

    public SilentGoContext(Response response, Request request) {
        this.response = response;
        this.request = request;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getHashString() {
        return hashString;
    }

    public void setHashString(String hashString) {
        this.hashString = hashString;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public JSONObject getParameterJson() {
        return parameterJson;
    }

    public void setParameterJson(JSONObject parameterJson) {
        this.parameterJson = parameterJson;
    }

    public JSONArray getParameterJsonArray() {
        return parameterJsonArray;
    }

    public void setParameterJsonArray(JSONArray parameterJsonArray) {
        this.parameterJsonArray = parameterJsonArray;
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }
}
