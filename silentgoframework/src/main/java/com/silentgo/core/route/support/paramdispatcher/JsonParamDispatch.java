package com.silentgo.core.route.support.paramdispatcher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.silentgo.core.SilentGo;
import com.silentgo.core.action.ActionParam;
import com.silentgo.core.config.Const;
import com.silentgo.core.exception.AppParameterPaserException;
import com.silentgo.core.exception.AppParameterResolverException;
import com.silentgo.core.route.ParameterDispatcher;
import com.silentgo.core.route.Route;
import com.silentgo.core.route.annotation.ParamDispatcher;
import com.silentgo.core.route.support.paramvalueresolve.ParameterResolveFactory;
import com.silentgo.servlet.http.ContentType;
import com.silentgo.servlet.http.Request;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.route.support.paramdispatcher
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/30.
 */
@ParamDispatcher
public class JsonParamDispatch implements ParameterDispatcher {
    @Override
    public Integer priority() {
        return 10;
    }

    @Override
    public void dispatch(ParameterResolveFactory parameterResolveFactory, ActionParam param, Route route, Object[] args) throws AppParameterResolverException, AppParameterPaserException {
        Request request = param.getRequest();

        Map<String, Object> hash = request.getHashMap();
        String jsonHash = JSON.toJSONString(hash);
        jsonHash = jsonHash.equals("{}") ? Const.EmptyString : jsonHash;
        SilentGo.getInstance().getConfig().getCtx().get().setHashString(jsonHash);

        ContentType type = ContentType.fromString(request.getContentType());
        if (type == null || !type.equals(ContentType.JSON)) {
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
            JSONObject jsonObject = JSON.parseObject(jsonString);
            request.setParameterJson(jsonObject);
            request.setJsonString(jsonString);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new AppParameterPaserException(e.getMessage());
        }
    }
}
