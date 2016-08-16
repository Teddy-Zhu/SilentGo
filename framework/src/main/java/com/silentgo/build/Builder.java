package com.silentgo.build;

import com.silentgo.core.SilentGo;

/**
 * Project : silentgo
 * com.silentgo.build
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/16.
 */
public abstract class Builder {

    public Integer priority() {
        return 100;
    }

    public abstract boolean build(SilentGo me);

}
