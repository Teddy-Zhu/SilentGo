package com.silentgo.core.route.support.paramdispatcher;

import com.silentgo.core.SilentGo;
import com.silentgo.core.build.SilentGoBuilder;
import com.silentgo.core.build.annotation.Builder;
import com.silentgo.core.exception.AppBuildException;
import org.apache.commons.io.FileCleaningTracker;

/**
 * Project : silentgo
 * com.silentgo.core.route.support.paramdispatcher
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/2.
 */
@Builder
public class MultiPartRelease extends SilentGoBuilder {

    @Override
    public Integer priority() {
        return 70;
    }

    public static final String FILE_CLEANING_TRACKER_ATTRIBUTE
            = MultiPartDispatch.class.getName() + ".FileCleaningTracker";

    @Override
    public boolean build(SilentGo me) throws AppBuildException {
        me.getContext().setAttribute(FILE_CLEANING_TRACKER_ATTRIBUTE , new FileCleaningTracker());
        return false;
    }
}
