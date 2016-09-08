package com.silentgo.core.action;

import com.silentgo.core.SilentGo;
import com.silentgo.core.action.annotation.Action;
import com.silentgo.servlet.http.HttpStatus;

/**
 * Project : silentgo
 * com.silentgo.core.action
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/7.
 */
@Action
public class ETagAction extends ActionChain {

    private static final long MAX_AGE = 2764800L;

    @Override
    public Integer priority() {
        return 5;
    }

    @Override
    public void doAction(ActionParam param) throws Throwable {
        if (SilentGo.getInstance().getConfig().getStaticFolder()
                .stream().anyMatch(url -> param.getRequestURL().startsWith(url))) {
            long ims = param.getRequest().getDateHeader("If-Modified-Since");
            long now = 0L;
            now = System.currentTimeMillis();
            if (ims + MAX_AGE > now) {
                param.getResponse().setStatus(HttpStatus.NOT_MODIFIED_304);
                return;
            }
            param.getResponse().setHeader("Cache-Control", "max-age=" + MAX_AGE);
            param.getResponse().addDateHeader("Expires", now + MAX_AGE * 1000);
            param.getResponse().addDateHeader("Last-Modified", now);
        }
        nextAction.doAction(param);
    }
}
