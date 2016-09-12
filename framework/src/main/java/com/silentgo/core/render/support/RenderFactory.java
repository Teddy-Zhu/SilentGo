package com.silentgo.core.render.support;

import com.silentgo.core.render.Render;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.utils.CollectionKit;

import java.util.HashMap;

/**
 * Project : silentgo
 * com.silentgo.core.render.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/30.
 */
public class RenderFactory extends BaseFactory {

    private HashMap<RenderType, Render> renderHashMap = new HashMap<>();

    public Render getRender(RenderType type) {
        return renderHashMap.get(type);
    }

    public boolean addAndReplaceRender(RenderType renderType, Render render) {
        if (renderHashMap.containsKey(renderType)) {
            renderHashMap.remove(renderType);
        }
        CollectionKit.MapAdd(renderHashMap, renderType, render);
        return true;
    }
}

