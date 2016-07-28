package com.silentgo.core.render;

import com.silentgo.config.SilentGoConfig;

/**
 * Project : silentgo
 * com.silentgo.core.render
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/25.
 */
public class RenderFactory {
    private SilentGoRender render;

    public RenderFactory(SilentGoRender render) {
        this.render = render;
    }

    public RenderFactory(SilentGoRender render, SilentGoConfig config) {
        this.render = render;
    }
}
