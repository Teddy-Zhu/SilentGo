package com.silentgo.core.render.support;

import com.silentgo.core.SilentGo;
import com.silentgo.core.build.SilentGoBuilder;
import com.silentgo.core.build.annotation.Builder;
import com.silentgo.core.config.Const;
import com.silentgo.core.exception.AppBuildException;

/**
 * Project : silentgo
 * com.silentgo.core.render.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/30.
 */
@Builder
public class RenderBuilder extends SilentGoBuilder {
    @Override
    public Integer priority() {
        return 55;
    }

    @Override
    public boolean build(SilentGo me) throws AppBuildException {

        RenderFactory renderFactory = new RenderFactory();

        me.getConfig().addFactory(renderFactory);

        renderFactory.addAndReplaceRender(RenderType.View, new JspRender(me.getConfig().getBaseView()));

        renderFactory.addAndReplaceRender(RenderType.JSON, new JsonRender(me.getConfig().getEncoding()));

        return true;
    }
}
