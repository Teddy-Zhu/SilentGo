package com.silentgo.core.route.support.requestdispatch;

import com.silentgo.core.SilentGo;
import com.silentgo.core.action.ActionParam;
import com.silentgo.core.build.Factory;
import com.silentgo.core.exception.AppBuildException;
import com.silentgo.core.exception.AppReleaseException;
import com.silentgo.core.exception.AppRequestParseException;
import com.silentgo.core.route.support.requestdispatch.annotation.RequestDispatch;
import com.silentgo.core.support.BaseFactory;
import org.apache.commons.io.FileCleaningTracker;

import java.util.ArrayList;
import java.util.List;

/**
 * Project : parent
 * Package : com.silentgo.core.route.support.requestdispatch
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/9.
 */
@Factory
public class RequestDispatchFactory extends BaseFactory {
    public static final String FILE_CLEANING_TRACKER_ATTRIBUTE
            = MultiPartDispatcher.class.getName() + ".FileCleaningTracker";

    private List<RequestDispatcher> requestDispatchers;

    public void dispatch(ActionParam param) throws AppRequestParseException {
        for (RequestDispatcher requestDispatcher : requestDispatchers) {
            requestDispatcher.dispatch(param);
        }
    }

    public void release(ActionParam param) throws AppRequestParseException {
        for (RequestDispatcher requestDispatcher : requestDispatchers) {
            requestDispatcher.release(param);
        }
    }

    @Override
    public boolean initialize(SilentGo me) throws AppBuildException {
        me.getContext().setAttribute(FILE_CLEANING_TRACKER_ATTRIBUTE, new FileCleaningTracker());


        requestDispatchers = new ArrayList<RequestDispatcher>();

        me.getAnnotationManager().getClasses(RequestDispatch.class).forEach(clz -> {
            if (RequestDispatcher.class.isAssignableFrom(clz)) {
                try {
                    requestDispatchers.add((RequestDispatcher) clz.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

        requestDispatchers.sort((o1, o2) -> {
            int x = o1.priority();
            int y = o2.priority();
            return (x < y) ? -1 : ((x == y) ? 0 : 1);
        });
        return true;
    }

    @Override
    public boolean destroy(SilentGo me) throws AppReleaseException {
        ((FileCleaningTracker) me.getContext().getAttribute(FILE_CLEANING_TRACKER_ATTRIBUTE)).exitWhenFinished();

        return true;
    }
}
