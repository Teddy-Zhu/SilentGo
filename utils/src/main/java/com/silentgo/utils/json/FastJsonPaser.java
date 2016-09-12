package com.silentgo.utils.json;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Array;
import java.util.List;

/**
 * Project : silentgo
 * com.silentgo.kit.json
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/1.
 */
public class FastJsonPaser implements JsonPaser {
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
}
