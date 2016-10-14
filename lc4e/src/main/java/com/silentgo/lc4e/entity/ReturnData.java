package com.silentgo.lc4e.entity;

/**
 * Project : SilentGo
 * Package : com.silentgo.lc4e.entity
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/10.
 */
public class ReturnData {

    private String name;
    private Object Data;

    public ReturnData() {
    }

    public ReturnData(String name, Object data) {
        this.name = name;
        Data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getData() {
        return Data;
    }

    public void setData(Object data) {
        Data = data;
    }
}