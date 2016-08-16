package com.silentgo.core.route.support;

import com.silentgo.core.SilentGo;
import com.silentgo.core.route.annotation.Controller;

import java.util.Set;

/**
 * Project : silentgo
 * com.silentgo.core.action.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/25.
 */
public class  RouteBuilder {
    public void Build(SilentGo me) {
        me.getAnnotationManager().getClasses(Controller.class).forEach(this::Build);
    }

    public void Build(Class<?> aClass) {
        Controller controller = (Controller) aClass.getAnnotation(Controller.class);

    }

    public boolean addRoute() {
        return true;
    }


}
