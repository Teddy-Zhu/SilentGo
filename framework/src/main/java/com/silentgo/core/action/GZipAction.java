package com.silentgo.core.action;

import com.silentgo.core.action.annotation.Action;
import com.silentgo.core.action.gzip.GZIPResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Project : parent
 * Package : com.silentgo.core.action
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/8.
 */
@Action
public class GZipAction extends ActionChain {

    private static final Logger LOGGER = LoggerFactory.getLogger(GZipAction.class);

    @Override
    public Integer priority() {
        return Integer.MIN_VALUE + 1;
    }

    @Override
    public void doAction(ActionParam param) throws Throwable {
        String ae = param.getRequest().getHeader("accept-encoding");
        //check if browser support gzip

        if (ae != null && ae.contains("gzip")) {
            GZIPResponseWrapper wrappedResponse = new GZIPResponseWrapper(param.getResponse());
            param.setResponse(wrappedResponse);
            nextAction.doAction(param);
            wrappedResponse.finishResponse();
        } else {
            nextAction.doAction(param);
        }
    }
}
