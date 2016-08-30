package com.silentgo.core.render.support;

/**
 * Project : silentgo
 * com.silentgo.core.render.support
 *
 * @author    <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by teddyzhu on 16/8/30.
 */
public enum RenderType {
    View("view"), JSON("json"), XML("xml"), TEXT("text"), FILE("file");
    private String type;

    RenderType(String contentType) {
        this.type = contentType;
    }
}
