package com.silentgo.core.action;

import com.silentgo.core.action.annotation.Action;
import com.silentgo.core.action.gzip.GZIPResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

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

    public static final String ACCEPT_ENCODING = "Accept-Encoding";


    public static final String CONTENT_ENCODING = "Content-Encoding";

    @Override
    public Integer priority() {
        return Integer.MIN_VALUE + 1;
    }

    @Override
    public void doAction(ActionParam param) throws Throwable {
        LOGGER.info("enter gzip action");

        String ae = param.getRequest().getHeader(ACCEPT_ENCODING);
        //check if browser support gzip

        if (ae != null && ae.toLowerCase().contains("gzip")) {
            param.getResponse().addHeader("Vary", ACCEPT_ENCODING);
            GZIPResponseWrapper wrappedResponse = new GZIPResponseWrapper(param.getResponse());
            param.setResponse(wrappedResponse);
            next(param);
            wrappedResponse.finish();
        } else {
            next(param);
        }
    }

}
