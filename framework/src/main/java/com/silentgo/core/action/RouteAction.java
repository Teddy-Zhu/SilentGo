package com.silentgo.core.action;

import java.io.IOException;

/**
 * Project : silentgo
 * com.silentgo.core.action
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by  on 16/7/18.
 */
public class RouteAction extends ActionChain {

    @Override
    public int priority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void doAction(ActionParam param) {

        try {
            param.getResponse().getWriter().write("aaaaaaaaa");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
