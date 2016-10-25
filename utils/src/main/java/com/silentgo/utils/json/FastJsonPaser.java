package com.silentgo.utils.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;

/**
 * Project : silentgo
 * com.silentgo.kit.json
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/1.
 */
public class FastJsonPaser implements JsonPaser<JSONObject> {

    public static final FastJsonPaser instance = new FastJsonPaser();

    @Override
    public JsonPaser me() {
        return instance;
    }

    @Override
    public JSONObject toJson(String json) {
        return JSON.parseObject(json.toString());
    }

    @Override
    public String toJsonString(Object object) {
        return JSON.toJSONString(object);
    }

    @Override
    public <T> T toObject(String json, Class<T> type) {
        return JSON.parseObject(json, type);
    }

    @Override
    public <T> T[] toObjectArray(String json, Class<T> type) {
        List<T> list = JSON.parseArray(json, type);
        return list.toArray((T[]) Array.newInstance(type, list.size()));
    }

    @Override
    public <T> List<T> toObjectList(String json, Class<T> type) {
        return JSON.parseArray(json, type);
    }

    @Override
    public <T> T toObject(String name, JSONObject jsonObject, Class<T> type) {
        return jsonObject.getObject(name, type);
    }

    @Override
    public <T> T[] toObjectArray(String name, JSONObject jsonObject, Class<T> type) {
        JSONArray array = jsonObject.getJSONArray(name);
        if (type.equals(array.getComponentType())) {
            return array.toArray((T[]) Array.newInstance(type, array.size()));
        }
        return null;
    }

    @Override
    public <T> Collection<T> toObjectList(String name, JSONObject jsonObject, Class<T> type) {
        return (Collection<T>) jsonObject.getJSONArray(name);
    }
}
