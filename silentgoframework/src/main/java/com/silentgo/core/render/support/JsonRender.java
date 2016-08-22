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
    private String contentType = "application/json; charset=" + encoding;

    public JsonRender(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public void render(Route route, Response response, Request request, Object retVal) throws AppRenderException {
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType(contentType);

        PrintWriter writer = null;
        try {
            writer = response.getWriter();

            writer.write(JSON.toJSONString(getAttrs(request)));
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
            throw new AppRenderException(e.getMessage());
        } finally {
            if (writer != null)
                writer.close();
        }
    }


    private Map<String, Object> getAttrs(Request request) {
        Map<String, Object> obj = new HashMap<>();

        Enumeration<String> stringEnumeration = request.getAttributeNames();

        while (stringEnumeration.hasMoreElements()) {
            String name = stringEnumeration.nextElement();
            obj.put(name, request.getAttribute(name));
        }
        return obj;
    }
}
