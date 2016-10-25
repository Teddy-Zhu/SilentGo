package com.silentgo.utils.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.Collection;

/**
 * Project : SilentGo
 * Package : com.silentgo.utils.json
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/25.
 */
public class GsonPaser implements JsonPaser<JsonElement> {
    private static final Gson gson = new Gson();

    public static final GsonPaser instance = new GsonPaser();

    @Override
    public JsonPaser me() {
        return instance;
    }

    @Override
    public JsonElement toJson(String json) {
        return new JsonParser().parse(json.toString());
    }

    @Override
    public String toJsonString(Object object) {
        return gson.toJson(object);
    }

    @Override
    public <T> T toObject(String json, Class<T> type) {
        return gson.fromJson(json, type);
    }

    @Override
    public <T> T[] toObjectArray(String json, Class<T> type) {
        return gson.fromJson(json, new TypeToken<T[]>() {
        }.getType());
    }

    @Override
    public <T> Collection<T> toObjectList(String json, Class<T> type) {
        return gson.fromJson(json, new TypeToken<Collection<T>>() {
        }.getType());
    }

    @Override
    public <T> T toObject(String name, JsonElement jsonElement, Class<T> type) {
        return gson.fromJson(jsonElement.getAsJsonObject().get(name), type);
    }

    @Override
    public <T> T[] toObjectArray(String name, JsonElement jsonElement, Class<T> type) {
        return gson.fromJson(jsonElement.getAsJsonObject().get(name), new TypeToken<T[]>() {
        }.getType());
    }

    @Override
    public <T> Collection<T> toObjectList(String name, JsonElement jsonElement, Class<T> type) {
        return gson.fromJson(jsonElement.getAsJsonObject().get(name), new TypeToken<Collection<T>>() {
        }.getType());
    }
}
