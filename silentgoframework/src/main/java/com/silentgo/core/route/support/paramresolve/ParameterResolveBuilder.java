package com.silentgo.core.route.support.paramresolve;

import com.silentgo.core.SilentGo;
import com.silentgo.core.build.SilentGoBuilder;
import com.silentgo.core.build.annotation.Builder;
import com.silentgo.core.route.ParameterValueResolver;
import com.silentgo.core.route.support.paramresolve.annotation.ParameterResolver;

/**
 * Project : silentgo
 * com.silentgo.core.route.support.paramresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/22.
 */
@Builder
public class ParameterResolveBuilder extends SilentGoBuilder {
    @Override
    public Integer priority() {
        return 60;
    }

    @Override
    public boolean build(SilentGo me) {

        ParameterResolveFactory parameterResolveFactory = new ParameterResolveFactory();
        me.getConfig().addFactory(parameterResolveFactory);
        me.getAnnotationManager().getClasses(ParameterResolver.class).forEach(aClass -> {
            if(!ParameterValueResolver.class.isAssignableFrom(aClass)) return;
            try {
                parameterResolveFactory.addParameterResolver((ParameterValueResolver) aClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return true;
    }
}
