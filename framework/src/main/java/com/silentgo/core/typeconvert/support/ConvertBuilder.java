package com.silentgo.core.typeconvert.support;

import com.silentgo.core.SilentGo;
import com.silentgo.core.build.SilentGoBuilder;
import com.silentgo.core.build.annotation.Builder;
import com.silentgo.core.typeconvert.ConvertKit;
import com.silentgo.core.typeconvert.ITypeConvertor;
import com.silentgo.core.typeconvert.annotation.Convertor;

/**
 * Project : silentgo
 * com.silentgo.kit.typeconvert.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/20.
 */
@Builder
public class ConvertBuilder extends SilentGoBuilder {
    @Override
    public Integer priority() {
        return 50;
    }

    @Override
    public boolean build(SilentGo me) {
        ConvertKit convertKit = new ConvertKit();
        me.getAnnotationManager().getClasses(Convertor.class)
                .forEach(aClass -> {
                    if (ITypeConvertor.class.isAssignableFrom(aClass))
                        convertKit.addConvert(aClass);
                });
        return true;
    }
}
