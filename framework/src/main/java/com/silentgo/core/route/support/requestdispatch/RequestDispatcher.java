package com.silentgo.core.route.support.requestdispatch;

import com.silentgo.core.action.ActionParam;
import com.silentgo.core.base.Priority;
import com.silentgo.core.exception.AppRequestParseException;

/**
 * Project : parent
 * Package : com.silentgo.core.route.support.requestdispatch
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/9.
 */
public interface RequestDispatcher extends Priority {

    @Override
    public default Integer priority() {
        return 1;
    }

    public default void release(ActionParam param) throws AppRequestParseException {
    }

    public void dispatch(ActionParam param) throws AppRequestParseException;

}
