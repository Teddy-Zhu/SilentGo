package com.silentgo.core.build;

import com.silentgo.core.base.Priority;
import com.silentgo.core.SilentGo;

/**
 * Project : silentgo
 * com.silentgo.core.build
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/16.
 */
public abstract class SilentGoBuilder implements Priority {

    public Integer priority() {
        return 100;
    }

    public abstract boolean build(SilentGo me);

}
