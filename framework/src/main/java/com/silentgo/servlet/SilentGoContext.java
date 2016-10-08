package com.silentgo.servlet;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.silentgo.core.action.ActionParam;
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

    ActionParam actionParam;


    //origin
    String hashString;

    JSONObject jsonObject;


    //JSON
    private JSONObject parameterJson;
    private JSONArray parameterJsonArray;

    private String jsonString;

    public SilentGoContext(ActionParam param) {
        this.actionParam = param;
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

    public ActionParam getActionParam() {
        return actionParam;
    }
}
