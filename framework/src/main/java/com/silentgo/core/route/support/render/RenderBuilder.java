package com.silentgo.core.route.support.render;

import com.silentgo.core.SilentGo;
import com.silentgo.core.build.SilentGoBuilder;
import com.silentgo.core.exception.AppBuildException;

/**
 * Project : silentgo
 * com.silentgo.core.route.support.render
 *
 * @author    <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by teddyzhu on 16/8/29.
 */
public class RenderBuilder extends SilentGoBuilder {

    @Override
    public Integer priority() {
        return 55;
    }

    @Override
    public boolean build(SilentGo me) throws AppBuildException {
        return true;
    }
}
