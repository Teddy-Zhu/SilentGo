package com.silentgo.core.render.renderresolver.support;

import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.render.RenderModel;
import com.silentgo.core.render.renderresolver.RenderResolver;
import com.silentgo.core.render.renderresolver.annotation.RenderResolve;
import com.silentgo.core.render.support.RenderFactory;
import com.silentgo.core.render.support.RenderType;
import com.silentgo.core.route.annotation.ResponseBody;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

/**
 * Project : silentgo
 * com.silentgo.core.render.renderresolver.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/30.
 */
@RenderResolve
public class ResponseBodyRenderResolver implements RenderResolver {
    @Override
    public boolean match(MethodAdviser adviser) {
        return adviser.existAnnotation(ResponseBody.class);
    }

    @Override
    public RenderModel getRenderModel(RenderFactory renderFactory, MethodAdviser adviser, Response response, Request request, Object retVal) {
        if (adviser.getMethod().getReturnType().equals(Void.class)) {
            return null;
        }
        return new RenderModel(renderFactory.getRender(RenderType.JSON), retVal, request, response);
    }

    @Override
    public Integer priority() {
        return 5;
    }
}
