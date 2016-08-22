package com.silentgo.servlet.http;

import com.silentgo.core.config.Const;
import com.silentgo.kit.CollectionKit;
import com.silentgo.kit.StringKit;

import javax.servlet.http.HttpServletRequestWrapper;
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

    private String[] pathParameters = new String[0];

    private Map<String, String> pathNamedParameters = new HashMap<>();

    private Map<String, Object> hashMap = new HashMap<>();

    public Request(javax.servlet.http.HttpServletRequest request) {
        super(request);
        this.paramsMap.putAll(request.getParameterMap());
        hashMap = getParamHash();
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


    public Map<String, String[]> getParameterMap() {
        return paramsMap;
    }

    public void setPathParameters(String[] pathParameters) {
        this.pathParameters = pathParameters;
    }

    public void setPathNamedParameters(Map<String, String> pathNamedParameters) {
        this.pathNamedParameters = pathNamedParameters;
    }

    public String getPathParameter(int index) {
        return pathParameters[index];
    }

    public String getPathParameter(String name) {
        return pathNamedParameters.get(name);
    }

    public void setParsedParamMap(Map<String, Object> parsedParamMap) {
        this.parsedParamMap = parsedParamMap;
    }


    private Map<String, Object> getParamHash() {

        Map<String, Object> prasedParamMap = new HashMap<>();

        paramsMap.forEach((k, v) -> {
            String[] ks = k.indexOf('.') > 0 ? k.split("\\.") : new String[]{k};
            if (ks.length == 0 || StringKit.isNullOrEmpty(ks[0])) return;

            if (ks.length == 1) {
                CollectionKit.MapAdd(prasedParamMap, ks[0], v[0]);
            }
            if (ks.length > 1) {
                Map<String, Object> map = getMap(prasedParamMap, ks[0]);
                for (int i = 1, len = ks.length - 1; i < len; i++) {
                    map = getMap(map, ks[i]);
                }
                CollectionKit.MapAdd(map, ks[ks.length - 1], v[0]);
            }
        });
        return prasedParamMap;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getMap(Map<String, Object> source, String key) {
        Object obj = source.get(key);
        if (obj == null) obj = new HashMap<>();
        source.put(key, obj);
        return (Map<String, Object>) obj;
    }

    public Map<String, Object> getHashMap() {
        return hashMap;
    }
}
