package com.silentgo.utils.json;

/**
 * Project : silentgo
 * com.silentgo.kit.json
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/1.
 */
public interface JsonPaser {

    public String toJsonString(Object object);

    public <T> T toObject(String json, Class<T> type);

    public <T> T[] toObjectArray(String json, Class<T> type);

}
