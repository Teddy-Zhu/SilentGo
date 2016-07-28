package com.silentgo.core.action;

/**
 * Project : silentgo
 * com.silentgo.core.IAction
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by  on 16/7/18.
 */
public abstract class ActionChain {

    public int priority() {
        return Integer.MAX_VALUE;
    }

    public ActionChain nextAction;

    public abstract void doAction(ActionParam param);
}
