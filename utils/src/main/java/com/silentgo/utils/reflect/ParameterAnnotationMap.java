package com.silentgo.utils.reflect;

import java.util.Map;

/**
 * Project : SilentGo
 * Package : com.silentgo.utils.reflect
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2017/1/5.
 */
public class ParameterAnnotationMap extends AnnotaionMap {

    private String[] parameterNames;

    private Map<String, SGParameter> parameterMap;

    public Map<String, SGParameter> getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(Map<String, SGParameter> parameterMap) {
        this.parameterMap = parameterMap;
    }

    public String[] getParameterNames() {
        return parameterNames;
    }

    public void setParameterNames(String[] parameterNames) {
        this.parameterNames = parameterNames;
    }
}
