package com.silentgo.core.action;

/**
 * Project : silentgo
 * com.silentgo.core.action
 *
 * @author <Acc href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</Acc>
 *         <p>
 *         Created by  on 16/7/18.
 */
public class StaticFileAction extends ActionChain {

    @Override
    public int priority() {
        return Integer.MIN_VALUE + 1;
    }

    @Override
    public void doAction(ActionParam param) {
        nextAction.doAction(param);
    }
}
