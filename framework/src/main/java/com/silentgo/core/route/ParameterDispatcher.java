package com.silentgo.core.route;

import com.silentgo.core.action.ActionParam;
import com.silentgo.core.base.Priority;
import com.silentgo.core.exception.AppParameterPaserException;
import com.silentgo.core.exception.AppParameterResolverException;
import com.silentgo.core.route.support.paramvalueresolve.ParameterResolveFactory;

/**
 * Project : silentgo
 * com.silentgo.core.route
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/22.
 */
public interface ParameterDispatcher extends Priority {

    default void release(ActionParam param){}

    public void dispatch(ParameterResolveFactory parameterResolveFactory, ActionParam param, Route route, Object[] args) throws AppParameterResolverException, AppParameterPaserException;
}
