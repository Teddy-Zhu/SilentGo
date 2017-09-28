package com.silentgo.core.action;

import com.silentgo.core.base.Priority;
import com.silentgo.core.config.Const;
import com.silentgo.core.ioc.bean.Bean;

/**
 * Project : silentgo
 * com.silentgo.core.IAction
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by  on 16/7/18.
 */
public abstract class ActionChain implements Priority {

    public Integer priority() {
        return Const.DefaultMax;
    }

    public Bean beanWrapper;

    public abstract void doAction(ActionParam param) throws Throwable;

    public void next(ActionParam param) throws Throwable {
        ((ActionChain) beanWrapper.getObject()).doAction(param);
    }
}
