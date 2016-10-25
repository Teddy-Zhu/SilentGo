package com.silentgo.utils.json;

import java.util.Collection;

/**
 * Project : silentgo
 * com.silentgo.kit.json
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/1.
 */
public interface JsonPaser<C> {
    public JsonPaser me();

    public C toJson(String json);

    public String toJsonString(Object object);

    public <T> T toObject(String json, Class<T> type);

    public <T> T[] toObjectArray(String json, Class<T> type);

    public <T> Collection<T> toObjectList(String json, Class<T> type);

    public <T> T toObject(String name, C c, Class<T> type);

    public <T> T[] toObjectArray(String name, C c, Class<T> type);

    public <T> Collection<T> toObjectList(String name, C c, Class<T> type);

}
