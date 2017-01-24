package com.silentgo.core.action;

import com.silentgo.core.SilentGo;
import com.silentgo.core.action.annotation.Action;
import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;

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


    private static final Log LOGGER = LogFactory.get();
    @Override
    public Integer priority() {
        return Integer.MAX_VALUE - 10;
    }

    @Override
    public void doAction(ActionParam param) throws Throwable {
        LOGGER.info("enter static action");
        SilentGo instance = SilentGo.me();
        String requestURL = param.getRequestURL();
        if (instance.getConfig().getStaticStartWith()
                .stream().anyMatch(requestURL::startsWith) ||
                instance.getConfig().getStaticEndWith().stream().anyMatch(requestURL::endsWith)) {

            // for static path mapping
            boolean forward = false;
            for (Map.Entry<String, String> entry : instance.getConfig().getStaticMapping().entrySet()) {
                if (requestURL.startsWith(entry.getKey())) {
                    forward = true;
                    param.getRequest().getRequestDispatcher(requestURL.replace(entry.getKey(), entry.getValue()))
                            .forward(param.getRequest(), param.getResponse());
                    break;
                }
            }


            if (!forward)
                param.doFilter(param.getRequest(), param.getResponse());
            return;
        }
        next(param);
    }
}
