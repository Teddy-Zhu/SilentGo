package com.silentgo.core.action.support;

import com.silentgo.core.SilentGo;
import com.silentgo.core.action.ActionChain;
import com.silentgo.core.action.RouteAction;
import com.silentgo.core.action.annotation.Action;
import com.silentgo.core.build.Factory;
import com.silentgo.core.exception.AppBuildException;
import com.silentgo.core.exception.AppReleaseException;
import com.silentgo.core.ioc.bean.BeanFactory;
import com.silentgo.core.ioc.bean.BeanWrapper;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.utils.CollectionKit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Project : silentgo
 * com.silentgo.core.action.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/26.
 */
@Factory
public class ActionFactory extends BaseFactory {

    @Override
    public boolean initialize(SilentGo me) throws AppBuildException {
        List<ActionChain> actionChains = new ArrayList<>(me.getConfig().getActionChains());
        me.getAnnotationManager().getClasses(Action.class).forEach(action -> {
            try {
                if (ActionChain.class.isAssignableFrom(action)) {
                    CollectionKit.ListAdd(actionChains, (ActionChain) action.newInstance());
                }
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        actionChains.sort((o1, o2) -> {
            int x = o1.priority();
            int y = o2.priority();
            return (x < y) ? -1 : ((x == y) ? 0 : 1);
        });

        ActionChain result = actionChains.get(actionChains.size() - 1);

        BeanFactory beanFactory = me.getFactory(me.getConfig().getBeanClass());
        BeanWrapper beanWrapper = beanFactory.addBean(result, true, false, false);
        if (result instanceof RouteAction) {
            for (int i = actionChains.size() - 1; i >= 0; i--) {
                ActionChain temp = actionChains.get(i);
                temp.beanWrapper = beanWrapper;
                beanWrapper = beanFactory.addBean(temp, true, false, false);
            }
        } else {
            throw new AppBuildException("the last action must be RouteAction");
        }

        me.getConfig().setActionChain(beanWrapper);
        return true;
    }

    @Override
    public boolean destroy(SilentGo me) throws AppReleaseException {
        return false;
    }
}
