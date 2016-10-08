package com.silentgo.core.config;

import com.silentgo.core.action.ActionChain;
import com.silentgo.core.cache.CacheManager;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.servlet.SilentGoContext;
import com.silentgo.utils.PropKit;

import java.util.HashMap;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.config
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/2.
 */
public class InterConfig {

    private final Map<Class<? extends BaseFactory>, BaseFactory> factoryMap = new HashMap<>();

    private final ThreadLocal<SilentGoContext> ctx = new ThreadLocal<>();
    private ActionChain actionChain;

    private PropKit propKit;

    private PropKit userProp;

    public Map<Class<? extends BaseFactory>, BaseFactory> getFactoryMap() {
        return factoryMap;
    }

    private Map<Class<? extends CacheManager>, CacheManager> cacheManagerMap = new HashMap<>();

    public Map<Class<? extends CacheManager>, CacheManager> getCacheManagerMap() {
        return cacheManagerMap;
    }

    public ThreadLocal<SilentGoContext> getCtx() {
        return ctx;
    }

    public ActionChain getActionChain() {
        return actionChain;
    }

    public void setActionChain(ActionChain actionChain) {
        this.actionChain = actionChain;
    }

    public PropKit getInnerPropKit() {
        return propKit;
    }

    public void setInnerPropKit(PropKit propKit) {
        this.propKit = propKit;
    }

    public PropKit getUserProp() {
        return userProp;
    }

    public void setUserProp(PropKit userProp) {
        this.userProp = userProp;
    }
}
