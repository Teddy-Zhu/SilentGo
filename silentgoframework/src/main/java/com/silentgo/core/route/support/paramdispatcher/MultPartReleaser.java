package com.silentgo.core.route.support.paramdispatcher;

import com.silentgo.core.SilentGo;
import com.silentgo.core.build.SilentGoReleaser;
import com.silentgo.core.build.annotation.Releaser;
import org.apache.commons.io.FileCleaningTracker;

/**
 * Project : silentgo
 * com.silentgo.core.route.support.paramdispatcher
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/2.
 */
@Releaser
public class MultPartReleaser extends SilentGoReleaser {
    public static final String FILE_CLEANING_TRACKER_ATTRIBUTE
            = MultiPartDispatch.class.getName() + ".FileCleaningTracker";

    @Override
    public void relase(SilentGo me) {
        ((FileCleaningTracker) me.getContext().getAttribute(FILE_CLEANING_TRACKER_ATTRIBUTE)).exitWhenFinished();
    }
}
