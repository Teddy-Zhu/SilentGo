package com.silentgo.core.support;

import com.silentgo.core.SilentGo;
import com.silentgo.core.exception.AppBuildException;
import com.silentgo.core.exception.AppReleaseException;

/**
 * Project : silentgo
 * com.silentgo.core.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/19.
 */
public abstract class BaseFactory {

    public abstract boolean initialize(SilentGo me) throws AppBuildException;

    public abstract boolean destroy(SilentGo me) throws AppReleaseException;
}
