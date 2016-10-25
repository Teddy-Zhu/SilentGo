package com.silentgo.core.route.support.requestdispatch;

import com.silentgo.core.SilentGo;
import com.silentgo.core.action.ActionParam;
import com.silentgo.core.exception.AppRequestParseException;
import com.silentgo.core.route.support.requestdispatch.annotation.RequestDispatch;
import com.silentgo.servlet.SilentGoContext;
import com.silentgo.servlet.http.ContentType;
import com.silentgo.servlet.http.Request;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Project : silentgo
 * com.silentgo.core.route.support.paramdispatcher
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/30.
 */
@RequestDispatch
public class JsonDispatcher implements RequestDispatcher {
    @Override
    public Integer priority() {
        return 10;
    }

    @Override
    public void dispatch(ActionParam param) throws AppRequestParseException {
        Request request = param.getRequest();

        SilentGo me = SilentGo.me();

        String hashString = me.json().toJsonString(request.getResolvedMap());
        SilentGoContext context = me.getConfig().getCtx().get();
        context.setHashString(hashString);

        if (request.getContentType() == null || !request.getContentType().contains(ContentType.JSON.toString())) {
            return;
        }
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return;
        }
        byte buffer[] = new byte[contentLength];
        for (int i = 0; i < contentLength; ) {
            int readlen = 0;
            try {
                readlen = request.getInputStream().read(buffer, i,
                        contentLength - i);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (readlen == -1) {
                break;
            }
            i += readlen;
        }
        try {
            String jsonString = new String(buffer, request.getCharacterEncoding());
            context.setJsonString(jsonString);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new AppRequestParseException(e.getMessage());
        }

        try {
            context.setJsonObject(me.json().toJson(context.getJsonString()));
        } catch (Exception e) {
            throw new AppRequestParseException("json string parse error");
        }
    }
}
