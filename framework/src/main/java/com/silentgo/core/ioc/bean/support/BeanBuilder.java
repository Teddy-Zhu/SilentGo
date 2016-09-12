package com.silentgo.core.ioc.bean.support;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.annotation.Aspect;
import com.silentgo.core.build.SilentGoBuilder;
import com.silentgo.core.build.annotation.Builder;
import com.silentgo.core.config.Const;
import com.silentgo.core.exception.annotaion.ExceptionHandler;
import com.silentgo.core.ioc.annotation.Component;
import com.silentgo.core.ioc.annotation.Service;
import com.silentgo.core.ioc.bean.BeanDefinition;
import com.silentgo.core.ioc.bean.BeanFactory;
import com.silentgo.core.ioc.bean.SilentGoBean;
import com.silentgo.core.route.annotation.Controller;
import com.silentgo.utils.logger.Logger;
import com.silentgo.utils.logger.LoggerFactory;

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
@Builder
public class BeanBuilder extends SilentGoBuilder {
    private static final Logger LOGGER = LoggerFactory.getLog(BeanBuilder.class);

    @Override
    public Integer priority() {
        return 10;
    }

    @Override
    public boolean build(SilentGo me) {
        List<BeanDefinition> beanDefinitions = new ArrayList<>();

        me.getAnnotationManager().getClasses(Service.class).forEach(aClass -> {
            if (aClass.isInterface()) return;
            Service service = (Service) aClass.getAnnotation(Service.class);
            if (!Const.DEFAULT_NONE.equals(service.value()))
                beanDefinitions.add(new BeanDefinition(service.value(), aClass));
            beanDefinitions.add(new BeanDefinition(aClass, true));
        });
        me.getAnnotationManager().getClasses(Component.class).forEach(aClass -> {
            if (aClass.isInterface()) return;
            Component component = (Component) aClass.getAnnotation(Component.class);
            if (!Const.DEFAULT_NONE.equals(component.value()))
                beanDefinitions.add(new BeanDefinition(component.value(), aClass));
            beanDefinitions.add(new BeanDefinition(aClass, true));
        });

        me.getAnnotationManager().getClasses(Controller.class).forEach(aClass -> {
            if (aClass.isInterface()) return;
            Controller controller = (Controller) aClass.getAnnotation(Controller.class);
            if (!Const.DEFAULT_NONE.equals(controller.value()))
                beanDefinitions.add(new BeanDefinition(controller.value(), aClass));
            beanDefinitions.add(new BeanDefinition(aClass, true));
        });

        me.getAnnotationManager().getClasses(Aspect.class).forEach(aClass -> {
            if (aClass.isInterface()) return;
            beanDefinitions.add(new BeanDefinition(aClass, false));
            Aspect aspect = (Aspect) aClass.getAnnotation(Aspect.class);
            if (!Const.DEFAULT_NONE.equals(aspect.value()))
                beanDefinitions.add(new BeanDefinition(aspect.value(), aClass, false));
        });

        me.getAnnotationManager().getClasses(ExceptionHandler.class).forEach(aClass -> {
            if (aClass.isInterface()) return;
            beanDefinitions.add(new BeanDefinition(aClass.getName(), aClass, false));
        });

        SilentGoBean beanFactory = new SilentGoBean();
        me.getConfig().addFactory(BeanFactory.class, beanFactory);
        beanFactory.build(beanDefinitions, me.getConfig());

        return true;
    }

}
