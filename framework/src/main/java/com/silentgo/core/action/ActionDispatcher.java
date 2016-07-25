package com.silentgo.core.action;

import java.util.*;

/**
 * Project : silentgo
 * com.silentgo.core.IAction
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by  on 16/7/18.
 */
public class ActionDispatcher {

    public static List<ActionChain> getActions() {
        List<ActionChain> all = new ArrayList<>();
        all.add(new RouteAction());
        all.add(new StaticFileAction());

        Collections.sort(all, (o1, o2) -> {
            int x = o1.priority();
            int y = o2.priority();
            return (x < y) ? -1 : ((x == y) ? 0 : 1);
        });

        return all;
    }

    public static ActionChain getAction() {

        List<ActionChain> actionChains = getActions();
        ActionChain result = actionChains.get(actionChains.size() - 1);

        if (result instanceof RouteAction) {
            for (int i = actionChains.size() - 1; i >= 0; i--) {
                ActionChain temp = actionChains.get(i);
                temp.nextAction = result;
                result = temp;
            }
        } else {
            throw new RuntimeException("the last action must be RouteAction");
        }

        return result;
    }
}
