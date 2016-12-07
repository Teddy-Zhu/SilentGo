package com.silentgo.core.route.support.requestdispatch;

import com.silentgo.core.SilentGo;
import com.silentgo.core.action.ActionParam;
import com.silentgo.core.config.Const;
import com.silentgo.core.config.FileUploadConfig;
import com.silentgo.core.route.support.requestdispatch.annotation.RequestDispatch;
import com.silentgo.servlet.http.MultiPartRequest;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.oreilly.MultipartRequestParser;
import com.silentgo.servlet.oreilly.multipart.AutoSaveFilePolicy;
import com.silentgo.servlet.oreilly.multipart.DefaultFileRenamePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * Project : silentgo
 * com.silentgo.core.route.support.paramdispatcher
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/1.
 */
@RequestDispatch
public class MultiPartDispatcher implements RequestDispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(MultiPartDispatcher.class);

    @Override
    public void release(ActionParam param) {
        if (param.getRequest() instanceof MultiPartRequest) {
            ((MultiPartRequest) param.getRequest()).delete();
        }
    }

    @Override
    public void dispatch(ActionParam param) {
        Request request = param.getRequest();

        String contentType = request.getContentType();

        if (contentType == null || !contentType.toLowerCase(Locale.ENGLISH).startsWith("multipart/")) {
            return;
        }

        FileUploadConfig config = (FileUploadConfig) SilentGo.me().getConfig().getConfig(Const.FileUploadConfig);


        MultiPartRequest multiPartRequest;
        try {
            multiPartRequest = MultipartRequestParser.parseMultiPartRequest(request, System.getProperty("java.io.tmpdir") + File.separator + "temp", config.getMaxSize(), request.getCharacterEncoding(), config.isAutoSave() ? new AutoSaveFilePolicy() : new DefaultFileRenamePolicy());
            param.setRequest(multiPartRequest);
        } catch (IOException e) {
            LOGGER.error("create multipartRequest failed", e);
            e.printStackTrace();
        }

    }

}
