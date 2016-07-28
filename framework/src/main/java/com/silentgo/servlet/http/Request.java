package com.silentgo.servlet.http;

import javax.servlet.http.*;
import java.util.HashMap;
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

    private Map<String, Object> parsedParamMap = new HashMap<>();

    public Request(javax.servlet.http.HttpServletRequest request) {
        super(request);
        this.paramsMap.putAll(request.getParameterMap());
    }


    public Request(javax.servlet.http.HttpServletRequest request, Map<String, Object> extraParams) {
        this(request);
        addAllParameters(extraParams);
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

    public Object getParsedParameter(String name) {
        return parsedParamMap.get(name);
    }

}
