package com.silentgo.core.route.support.annotationResolver;

import com.silentgo.core.SilentGo;
import com.silentgo.core.build.SilentGoBuilder;
import com.silentgo.core.build.annotation.Builder;
import com.silentgo.core.exception.AppBuildException;
import com.silentgo.core.route.support.annotationResolver.annotation.ParamAnResolver;

/**
 * Project : silentgo
 * com.silentgo.core.route.support.annotationResolver
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/30.
 */
@Builder
public class ParamAnBuilder extends SilentGoBuilder {

    @Override
    public Integer priority() {
        return 60;
    }

    @Override
    public boolean build(SilentGo me) throws AppBuildException {
        ParamAnFactory paramAnFactory = new ParamAnFactory();
        me.getConfig().addFactory(paramAnFactory);

        me.getAnnotationManager().getClasses(ParamAnResolver.class).forEach(aClass -> {
            if (ParamAnnotationResolver.class.isAssignableFrom(aClass)) {
                try {
                    paramAnFactory.addResolver((ParamAnnotationResolver) aClass.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        return false;
    }
}
