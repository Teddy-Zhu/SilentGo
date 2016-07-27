package com.silentgo.core.route.support;

import com.silentgo.core.route.annotation.Controller;

import java.util.Set;

/**
 * Project : silentgo
 * com.silentgo.core.action.support
 *
 * @author <Acc href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</Acc>
 *         <p>
 *         Created by teddyzhu on 16/7/25.
 */
public class RouteBuilder {

    public void build(Set<Class> classes) {
        classes.forEach(aClass -> {
            Controller controller = (Controller) aClass.getAnnotation(Controller.class);


        });
    }

    public boolean addRoute() {
        return true;
    }


}
