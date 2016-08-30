package com.silentgo.core.render.renderresolver;

import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.base.Priority;
import com.silentgo.core.render.RenderModel;
import com.silentgo.core.render.support.RenderFactory;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

/**
 * Project : silentgo
 * com.silentgo.core.render.renderresolver
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/30.
 */
public interface RenderResolver extends Priority {

    public boolean match(MethodAdviser adviser);

    public RenderModel getRenderModel(RenderFactory renderFactory, MethodAdviser adviser, Response response, Request request, Object retVal);

}
