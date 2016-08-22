package com.silentgo.core.route.support;

import com.silentgo.core.route.ParameterDispatcher;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.kit.CollectionKit;

import java.util.ArrayList;
import java.util.List;

/**
 * Project : silentgo
 * com.silentgo.core.route.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/22.
 */
public class ParamDispatchFactory implements BaseFactory {

    private List<ParameterDispatcher> dispatcher = new ArrayList<>();

    public boolean addDispatcher(ParameterDispatcher parameterDispatcher) {
        return addDispatcher(parameterDispatcher, false);
    }

    public boolean addDispatcher(ParameterDispatcher parameterDispatcher, boolean resort) {
        boolean ret = CollectionKit.ListAdd(dispatcher, parameterDispatcher);
        if (resort) resort();
        return ret;
    }

    public void resort() {
        dispatcher.sort((o1, o2) -> {
            int x = o1.priority();
            int y = o2.priority();
            return (x < y) ? -1 : ((x == y) ? 0 : 1);
        });
    }

    public List<ParameterDispatcher> getDispatchers() {
        return dispatcher;
    }
}
