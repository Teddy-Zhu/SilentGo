package com.silentgo.core.action;

import com.silentgo.core.SilentGo;
import com.silentgo.core.action.annotation.Action;
import com.silentgo.servlet.http.HttpStatus;

import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.action
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by  on 16/7/18.
 */
@Action
public class StaticFileAction extends ActionChain {

    private static final long MAX_AGE = 2764800L;

    @Override
    public Integer priority() {
        return Integer.MAX_VALUE - 10;
    }

    @Override
    public void doAction(ActionParam param) throws Throwable {
        SilentGo instance = SilentGo.getInstance();
        String requestURL = param.getRequestURL();
        if (instance.getConfig().getStaticStartWith()
                .stream().anyMatch(requestURL::startsWith) ||
                instance.getConfig().getStaticEndWith().stream().anyMatch(requestURL::endsWith)) {
            //handle e tag for static file
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


            // for static path mapping
            boolean forward = false;
            for (Map.Entry<String, String> entry : instance.getConfig().getStaticMapping().entrySet()) {
                if (requestURL.startsWith(entry.getKey())) {
                    forward = true;
                    param.getRequest().getRequestDispatcher(requestURL.replace(entry.getKey(), entry.getValue()))
                            .forward(param.getRequest().getRequest(), param.getResponse().getResponse());
                    break;
                }
            }



            if (!forward)
                param.getFilterChain().doFilter(param.getRequest(), param.getResponse());
            return;
        }
        nextAction.doAction(param);
    }
}
