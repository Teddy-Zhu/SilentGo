package com.silentgo.core.route.support.paramdispatcher;

import com.silentgo.core.SilentGo;
import com.silentgo.core.action.ActionParam;
import com.silentgo.core.build.Factory;
import com.silentgo.core.exception.AppBuildException;
import com.silentgo.core.exception.AppParameterPaserException;
import com.silentgo.core.exception.AppParameterResolverException;
import com.silentgo.core.exception.AppReleaseException;
import com.silentgo.core.route.ParameterDispatcher;
import com.silentgo.core.route.Route;
import com.silentgo.core.route.support.paramvalueresolve.ParameterResolveFactory;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.utils.CollectionKit;
import org.apache.commons.io.FileCleaningTracker;

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
@Factory
public class ParamDispatchFactory extends BaseFactory {
    public static final String FILE_CLEANING_TRACKER_ATTRIBUTE
            = MultiPartDispatch.class.getName() + ".FileCleaningTracker";

    private List<ParameterDispatcher> dispatchers = new ArrayList<>();

    public boolean addDispatcher(ParameterDispatcher parameterDispatcher) {
        return addDispatcher(parameterDispatcher, false);
    }

    public boolean addDispatcher(ParameterDispatcher parameterDispatcher, boolean resort) {
        boolean ret = CollectionKit.ListAdd(dispatchers, parameterDispatcher);
        if (resort) resort();
        return ret;
    }

    public void resort() {
        dispatchers.sort((o1, o2) -> {
            int x = o1.priority();
            int y = o2.priority();
            return (x < y) ? -1 : ((x == y) ? 0 : 1);
        });
    }

    public List<ParameterDispatcher> getDispatchers() {
        return dispatchers;
    }

    public void dispatch(ParameterResolveFactory parameterResolveFactory, ActionParam param, Route route, Object[] args) throws AppParameterResolverException, AppParameterPaserException {
        for (ParameterDispatcher parameterDispatcher : dispatchers) {
            parameterDispatcher.dispatch(parameterResolveFactory, param, route, args);
        }
    }

    public void release(ActionParam param) {
        dispatchers.forEach(dispatcher -> dispatcher.release(param));
    }

    @Override
    public boolean initialize(SilentGo me) throws AppBuildException {
        me.getContext().setAttribute(FILE_CLEANING_TRACKER_ATTRIBUTE, new FileCleaningTracker());
        return true;
    }

    @Override
    public boolean destroy(SilentGo me) throws AppReleaseException {
        ((FileCleaningTracker) me.getContext().getAttribute(FILE_CLEANING_TRACKER_ATTRIBUTE)).exitWhenFinished();
        return true;
    }
}
