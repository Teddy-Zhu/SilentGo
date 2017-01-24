package com.silentgo.utils.json;

import com.silentgo.json.JSON;
import com.silentgo.json.model.JSONEntity;

import java.util.Collection;

/**
 * Project : SilentGo
 * Package : com.silentgo.utils.json
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2017/1/23.
 */
public class SilentGoParser implements JsonPaser<JSONEntity> {

    private static final JsonPaser me = new SilentGoParser();

    @Override
    public JsonPaser me() {
        return me;
    }

    @Override
    public JSONEntity toJson(String json) {
        return JSON.parse(json);
    }

    @Override
    public String toJsonString(Object object) {
        return JSON.toJSONString(object);
    }

    @Override
    public <T> T toObject(String json, Class<T> type) {
        return JSON.parse(json, type);
    }

    @Override
    public <T> T[] toObjectArray(String json, Class<T> type) {
        return JSON.parseArray(json, type);
    }

    @Override
    public <T> Collection<T> toObjectList(String json, Class<T> type) {
        return JSON.parseCollection(json, type);
    }

    @Override
    public <T> T toObject(String name, JSONEntity jsonEntity, Class<T> type) {
        return JSON.parse(jsonEntity, type, name);
    }

    @Override
    public <T> T[] toObjectArray(String name, JSONEntity jsonEntity, Class<T> type) {
        return JSON.parseArray(jsonEntity, type, name);
    }

    @Override
    public <T> Collection<T> toObjectList(String name, JSONEntity jsonEntity, Class<T> type) {
        return JSON.parseCollection(jsonEntity, type, name);
    }
}
