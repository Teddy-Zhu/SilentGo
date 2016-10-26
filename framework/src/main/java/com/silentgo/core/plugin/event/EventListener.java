package com.silentgo.core.plugin.event;

/**
 * Project : SilentGo
 * Package : com.silentgo.core.plugin.event
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/26.
 */
public interface EventListener<T extends Event> {

    public void onEvent(T event, Object... objects);

}
