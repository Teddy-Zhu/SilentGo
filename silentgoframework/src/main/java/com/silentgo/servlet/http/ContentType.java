package com.silentgo.servlet.http;

import com.silentgo.kit.StringKit;

/**
 * Project : silentgo
 * com.silentgo.servlet.http
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/30.
 */
public enum ContentType {
    JSON("application/json"), XML("application/xml");

    private String contentType;

    ContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return this.contentType;
    }

    public static ContentType fromString(String contentType) {
        if (StringKit.isNullOrEmpty(contentType)) return null;
        for (ContentType t : ContentType.values()) {
            if (contentType.equalsIgnoreCase(t.contentType)) {
                return t;
            }
        }
        return null;
    }

    public static ContentType fromString(String contentType, ContentType defaultType) {
        if (contentType != null) {
            for (ContentType t : ContentType.values()) {
                if (contentType.equalsIgnoreCase(t.contentType)) {
                    return t;
                }
            }
        }
        return defaultType;
    }

    @Override
    public String toString() {
        return contentType;
    }


}