package com.silentgo.core.plugin.event;

import com.silentgo.core.SilentGo;

/**
 * Project : SilentGo
 * Package : com.silentgo.core.plugin.event
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/28.
 */
public class EventTool {

    public static void push(Event event, Object... objects) {
        SilentGo.me().getFactory(EventFactory.class).emit(event, objects);
    }
}
