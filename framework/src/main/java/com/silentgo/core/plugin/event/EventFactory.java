package com.silentgo.core.plugin.event;

import com.silentgo.core.SilentGo;
import com.silentgo.core.build.Factory;
import com.silentgo.core.exception.AppBuildException;
import com.silentgo.core.exception.AppReleaseException;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.utils.ClassKit;
import com.silentgo.utils.CollectionKit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Project : SilentGo
 * Package : com.silentgo.core.plugin.event
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/26.
 */
@Factory
public class EventFactory extends BaseFactory {

    private Map<Class<? extends Event>, List<EventListener>> eventMap = new ConcurrentHashMap<>();

    @Override
    public boolean initialize(SilentGo me) throws AppBuildException {
        me.getAnnotationManager().getClasses(com.silentgo.core.plugin.event.annotation.EventListener.class).forEach(clz -> {
            if (EventListener.class.isAssignableFrom(clz)) {

                Class<? extends Event> event = (Class<? extends Event>) ClassKit.getGenericClass(clz, 0);
                try {
                    EventListener eventListener = (EventListener) clz.newInstance();
                    CollectionKit.ListMapAdd(eventMap, event, eventListener);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        me.getConfig().getEventListeners().forEach(clz -> {
            Class<? extends Event> event = (Class<? extends Event>) ClassKit.getGenericClass(clz, 0);
            try {
                EventListener eventListener = clz.newInstance();
                CollectionKit.ListMapAdd(eventMap, event, eventListener);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return true;
    }

    @Override
    public boolean destroy(SilentGo me) throws AppReleaseException {
        eventMap.clear();
        return true;
    }

    public void emit(Event event, Object... objects) {
        for (EventListener eventListener : eventMap.getOrDefault(event, new ArrayList<>())) {
            eventListener.onEvent(event, objects);
        }
    }

}
