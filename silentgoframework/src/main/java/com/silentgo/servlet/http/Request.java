package com.silentgo.servlet.http;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.silentgo.core.config.Const;
import com.silentgo.kit.CollectionKit;
import com.silentgo.kit.StringKit;

import javax.servlet.http.HttpServletRequestWrapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.servlet.web
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by  on 16/7/18.
 */
public class Request extends HttpServletRequestWrapper {

    private Map<String, String[]> paramsMap = new HashMap<>();

    private Map<String, Object> resolvedMap;

    private int pathSize = 0;
    private String[] pathParameters = new String[0];


    private Map<String, String> pathNamedParameters = new HashMap<>();

    public Request(javax.servlet.http.HttpServletRequest request) {
        super(request);
        this.paramsMap.putAll(request.getParameterMap());
        resolvedMap = RequestKit.parse(paramsMap);
    }

    @Override
    public String getParameter(String name) {
        String[] values = paramsMap.get(name);
        if (values == null || values.length == 0) {
            return null;
        }
        return values[0];
    }

    public String[] getParameterValues(String name) {
        return paramsMap.get(name);
    }

    public void addAllParameters(Map<String, Object> otherParams) {
        for (Map.Entry<String, Object> entry : otherParams.entrySet()) {
            addParameter(entry.getKey(), entry.getValue());
        }
    }


    public void addParameter(String name, Object value) {
        if (value != null) {
            if (value instanceof String[]) {
                paramsMap.put(name, (String[]) value);
            } else if (value instanceof String) {
                paramsMap.put(name, new String[]{(String) value});
            } else {
                paramsMap.put(name, new String[]{String.valueOf(value)});
            }
        }
    }


    public Map<String, String[]> getParameterMap() {
        return paramsMap;
    }

    public void setPathParameters(String[] pathParameters) {
        pathSize = pathParameters.length;
        this.pathParameters = pathParameters;
    }

    public void setPathNamedParameters(Map<String, String> pathNamedParameters) {
        this.pathNamedParameters = pathNamedParameters;
    }

    public String getPathParameter(int index) {
        return index > pathSize ? null : pathParameters[index];
    }

    public String getPathParameter(String name) {
        return pathNamedParameters.get(name);
    }

    public Map<String, Object> getResolvedMap() {
        return resolvedMap;
    }
}
