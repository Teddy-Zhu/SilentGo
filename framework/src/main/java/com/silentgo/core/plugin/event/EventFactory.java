package com.silentgo.core.plugin.event;

import com.silentgo.core.SilentGo;
import com.silentgo.core.build.Factory;
import com.silentgo.core.exception.AppBuildException;
import com.silentgo.core.exception.AppReleaseException;
import com.silentgo.core.ioc.bean.BeanDefinition;
import com.silentgo.core.ioc.bean.BeanFactory;
import com.silentgo.core.ioc.bean.BeanWrapper;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.utils.ClassKit;
import com.silentgo.utils.CollectionKit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

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

    private Map<Class<? extends Event>, List<EventExecutor>> eventSyncMap = new HashMap<>();

    private Map<Class<? extends Event>, List<EventExecutor>> eventASyncMap = new HashMap<>();

    @Override
    public boolean initialize(SilentGo me) throws AppBuildException {
        BeanFactory beanFactory = me.getFactory(me.getConfig().getBeanClass());

        me.getAnnotationManager().getClasses(com.silentgo.core.plugin.event.annotation.EventListener.class).forEach(clz -> {
            if (EventListener.class.isAssignableFrom(clz)) {
                com.silentgo.core.plugin.event.annotation.EventListener eventListenerAn = (com.silentgo.core.plugin.event.annotation.EventListener) clz.getAnnotation(com.silentgo.core.plugin.event.annotation.EventListener.class);
                Class<? extends Event> event = (Class<? extends Event>) ClassKit.getGenericClass(clz, 0);
                try {
                    EventListener eventListener = (EventListener) clz.newInstance();
                    EventExecutor eventExecutor = new EventExecutor((EventListener) beanFactory.addBean(eventListener, false, true, true).getObject());
                    eventExecutor.setAsync(eventListenerAn.async());
                    eventExecutor.setDelay(eventListenerAn.delayTime());
                    if (eventExecutor.isAsync()) {
                        CollectionKit.ListMapAdd(eventASyncMap, event, eventExecutor);
                    } else {
                        CollectionKit.ListMapAdd(eventSyncMap, event, eventExecutor);
                    }
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        me.getConfig().getEventListeners().forEach(clz -> {
            Class<? extends Event> event = (Class<? extends Event>) ClassKit.getGenericClass(clz, 0);
            try {
                EventListener eventListener = clz.newInstance();
                EventExecutor eventExecutor = new EventExecutor((EventListener) beanFactory.addBean(eventListener, false, true, true).getObject());
                eventExecutor.setAsync(true);
                eventExecutor.setDelay(0);
                CollectionKit.ListMapAdd(eventSyncMap, event, eventExecutor);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return true;
    }

    @Override
    public boolean destroy(SilentGo me) throws AppReleaseException {
        eventSyncMap.clear();
        eventASyncMap.clear();
        return true;
    }

    public void emit(Event event) {
        try {
            for (EventExecutor eventExecutor : eventSyncMap.getOrDefault(event, new ArrayList<>())) {
                eventExecutor.run(event);
            }
            for (EventExecutor eventExecutor : eventASyncMap.getOrDefault(event, new ArrayList<>())) {
                eventExecutor.run(event);
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
