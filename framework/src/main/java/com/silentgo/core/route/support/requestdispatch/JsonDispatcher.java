package com.silentgo.core.route.support.requestdispatch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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

        String jsonHash = JSON.toJSONString(request.getResolvedMap());
        JSONObject jsonObjectParam = JSON.parseObject(jsonHash);
        SilentGoContext context = SilentGo.getInstance().getConfig().getCtx().get();
        context.setJsonObject(jsonObjectParam);
        context.setHashString(jsonHash);

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
            if (jsonString.startsWith("[")) {
                JSONArray jsonArray = JSON.parseArray(jsonString);
                context.setParameterJsonArray(jsonArray);
            } else {
                JSONObject jsonObject = JSON.parseObject(jsonString);
                context.setParameterJson(jsonObject);
            }
            context.setJsonString(jsonString);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new AppRequestParseException(e.getMessage());
        }
    }
}
