package com.silentgo.core.render.support;

import com.alibaba.fastjson.JSON;
import com.silentgo.core.action.ActionParam;
import com.silentgo.core.exception.AppRenderException;
import com.silentgo.core.render.Render;
import com.silentgo.core.route.Route;
import com.silentgo.kit.StringKit;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;
import sun.dc.pr.PRError;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.render.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/22.
 */
public class JsonRender implements Render {

    private String encoding;
    private String contentType = encoding;

    public JsonRender(String encoding) {
        this.encoding = encoding;
        this.contentType = "application/json; charset=" + encoding;
    }

    @Override
    public void render(Response response, Request request, Object retVal) throws AppRenderException {
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache , no-store , mag-age=0");
        response.setDateHeader("Expires", 0);
        response.setContentType(contentType);

        PrintWriter writer = null;
        try {
            writer = response.getWriter();

            writer.write(retVal instanceof String ? retVal.toString() : JSON.toJSONString(retVal));
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
            throw new AppRenderException(e.getMessage());
        } finally {
            if (writer != null)
                writer.close();
        }
    }

}
