package com.silentgo.core.ioc.bean.support;

import com.silentgo.config.Const;
import com.silentgo.core.SilentGo;
import com.silentgo.core.ioc.annotation.Component;
import com.silentgo.core.ioc.annotation.Service;
import com.silentgo.core.ioc.bean.BeanDefinition;
import com.silentgo.core.ioc.bean.SilentGoBean;
import com.silentgo.core.route.annotation.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Project : silentgo
 * com.silentgo.core.ioc.bean.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/26.
 */
public class BeanBuilder {

    public static void Build(SilentGo me) {
        List<BeanDefinition> beanDefinitions = new ArrayList<>();

        me.getAnnotationManager().getClasses(Service.class).forEach(aClass -> {
            Service service = (Service) aClass.getAnnotation(Service.class);
            beanDefinitions.add(new BeanDefinition(Const.DEFAULT_NONE.equals(service.value()) ? aClass.getName() : service.value(), aClass));
        });
        me.getAnnotationManager().getClasses(Component.class).forEach(aClass -> {
            Component component = (Component) aClass.getAnnotation(Component.class);
            beanDefinitions.add(new BeanDefinition(Const.DEFAULT_NONE.equals(component.value()) ? aClass.getName() : component.value(), aClass));
        });

        me.getAnnotationManager().getClasses(Controller.class).forEach(aClass -> {
            beanDefinitions.add(new BeanDefinition(aClass));
        });


        //noinspection unchecked
        me.getConfig().getBeanFactory().build(beanDefinitions,me.getConfig());

    }

}
