package com.silentgo.core.action;

import com.silentgo.core.SilentGo;
import com.silentgo.core.action.annotation.Action;

/**
 * Project : silentgo
 * com.silentgo.core.action
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by  on 16/7/18.
 */
@Action
public class StaticFileAction extends ActionChain {

    @Override
    public Integer priority() {
        return Integer.MIN_VALUE + 1;
    }

    @Override
    public void doAction(ActionParam param) throws Throwable {
        if (SilentGo.getInstance().getConfig().getStaticFolder()
                .stream().anyMatch(url -> param.getRequestURL().startsWith(url))) {
            return;
        }
        nextAction.doAction(param);
    }
}
