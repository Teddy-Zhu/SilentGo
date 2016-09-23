package com.silentgo.core.build;

import com.silentgo.core.SilentGo;
import com.silentgo.core.base.Priority;

/**
 * Project : silentgo
 * com.silentgo.core.build
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/2.
 */
public abstract class SilentGoReleaser implements Priority {
    @Override
    public Integer priority() {
        return 100;
    }

    public abstract void release(SilentGo me);
}
