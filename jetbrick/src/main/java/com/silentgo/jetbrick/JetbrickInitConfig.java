package com.silentgo.jetbrick;

import com.silentgo.core.SilentGo;
import com.silentgo.core.config.Config;
import com.silentgo.core.config.SilentGoConfig;
import com.silentgo.core.render.support.RenderFactory;
import com.silentgo.core.render.support.RenderType;

import java.util.Properties;

/**
 * Project : SilentGo
 * Package : com.silentgo.jetbrick
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/14.
 */
public class JetbrickInitConfig implements Config {

    Properties properties;

    public JetbrickInitConfig(Properties properties) {
        this.properties = properties;
    }

    public JetbrickInitConfig() {
    }

    @Override
    public void afterInit(SilentGoConfig config) {
        if (properties == null) {
            properties = config.getUserProp().getProperties();
        }
        RenderFactory renderFactory = config.getFactory(RenderFactory.class, SilentGo.me());
        JetTemplateRender jetTemplateRender = new JetTemplateRender(properties);
        renderFactory.addAndReplaceRender(RenderType.View, jetTemplateRender);
    }
}
