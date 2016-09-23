package com.silentgo.core.ioc.bean.support;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.annotation.Aspect;
import com.silentgo.core.build.SilentGoBuilder;
import com.silentgo.core.build.annotation.Builder;
import com.silentgo.core.exception.annotaion.ExceptionHandler;
import com.silentgo.core.ioc.annotation.Component;
import com.silentgo.core.ioc.annotation.Service;
import com.silentgo.core.ioc.bean.BeanDefinition;
import com.silentgo.core.ioc.bean.BeanFactory;
import com.silentgo.core.ioc.bean.SilentGoBean;
import com.silentgo.core.plugin.db.bridge.BaseDao;
import com.silentgo.core.plugin.db.bridge.DaoInterceptor;
import com.silentgo.core.route.annotation.Controller;
import com.silentgo.utils.logger.Logger;
import com.silentgo.utils.logger.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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


        commonBuild(me, beanDefinitions, Service.class);

        commonBuild(me, beanDefinitions, Component.class);

        commonBuild(me, beanDefinitions, Controller.class);

        commonBuild(me, beanDefinitions, Aspect.class, false);


        me.getAnnotationManager().getClasses(ExceptionHandler.class).forEach(aClass -> {
            if (aClass.isInterface()) return;
            beanDefinitions.add(new BeanDefinition(aClass.getName(), aClass, false));
        });

        SilentGoBean beanFactory = new SilentGoBean();
        me.getConfig().addFactory(BeanFactory.class, beanFactory);
        beanFactory.build(beanDefinitions, me.getConfig());

        return true;
    }

    private void buildInterface(List<BeanDefinition> beanDefinitions, Class<?> clz) {
        if (BaseDao.class.isAssignableFrom(clz)) {
            beanDefinitions.add(new BeanDefinition(clz, DaoInterceptor.proxy(clz), false, true));
        }
    }

    private <T extends Annotation> void commonBuild(SilentGo me, List<BeanDefinition> beanDefinitions, Class<T> t) {
        commonBuild(me, beanDefinitions, t, true);
    }

    private <T extends Annotation> void commonBuild(SilentGo me, List<BeanDefinition> beanDefinitions, Class<T> t, boolean inject) {
        me.getAnnotationManager().getClasses(t).forEach(aClass -> {
            if (aClass.isInterface()) {
                buildInterface(beanDefinitions, aClass);
                return;
            }
            T annotation = (T) aClass.getAnnotation(aClass);
            try {
                Method method = aClass.getDeclaredMethod("value");
                beanDefinitions.add(new BeanDefinition(method.invoke(annotation).toString(), aClass, inject));
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
            beanDefinitions.add(new BeanDefinition(aClass, inject));
        });

    }
}
