package com.silentgo.core.plugin.event;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Project : SilentGo
 * Package : com.silentgo.core.plugin.event
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/28.
 */
public class EventExecutor {

    private EventListener eventListener;
    private int delay = 0;
    private boolean async = false;

    public EventExecutor(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public EventExecutor(EventListener eventListener, int delay, boolean async) {
        this.eventListener = eventListener;
        this.delay = delay;
        this.async = async;
    }

    public void run(Event event, Object... objects) throws ExecutionException, InterruptedException {
        Callable<Boolean> callable = (Callable<Boolean>) () -> {
            Thread.sleep(delay);
            eventListener.onEvent(event, objects);
            return true;
        };
        FutureTask<Boolean> future = new FutureTask<>(callable);
        if (!async) {
            future.get();
        }
    }

    public EventListener getEventListener() {
        return eventListener;
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }
}
