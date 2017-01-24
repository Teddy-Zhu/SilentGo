package com.silentgo.jetbrick;

import com.silentgo.core.SilentGo;
import com.silentgo.core.exception.AppRenderException;
import com.silentgo.core.render.Render;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;
import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;
import jetbrick.template.JetEngine;
import jetbrick.template.JetTemplate;
import jetbrick.template.web.JetWebContext;
import jetbrick.template.web.JetWebEngine;

import java.io.IOException;
import java.util.Properties;

/**
 * Project : SilentGo
 * Package : PACKAGE_NAME
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/10.
 */
public class JetTemplateRender implements Render {


    private static final Log LOGGER = LogFactory.get();

    private final JetEngine engine;

    public JetTemplateRender() {
        engine = JetWebEngine.create(SilentGo.me().getContext());
    }

    public JetTemplateRender(Properties properties) {
        engine = JetWebEngine.create(SilentGo.me().getContext(), properties, null);
    }


    @Override
    public void render(Response response, Request request, Object retVal) throws AppRenderException {
        Long start = System.currentTimeMillis();
        String charsetEncoding = engine.getConfig().getOutputEncoding().name();
        response.setCharacterEncoding(charsetEncoding);
        if (response.getContentType() == null) {
            response.setContentType("text/html; charset=" + charsetEncoding);
        }

        try {
            JetTemplate template = engine.getTemplate(retVal.toString());
            JetWebContext context = new JetWebContext(request, response);
            template.render(context, response.getOutputStream());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        LOGGER.debug("end jetbrick template render : {}", System.currentTimeMillis() - start);
    }

    public JetEngine getEngine() {
        return engine;
    }
}
