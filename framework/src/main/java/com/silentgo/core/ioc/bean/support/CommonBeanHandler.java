package com.silentgo.core.ioc.bean.support;

import com.silentgo.core.ioc.annotation.Component;
import com.silentgo.core.ioc.annotation.Service;
import com.silentgo.core.ioc.bean.BeanDefinition;
import com.silentgo.core.route.annotation.Controller;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Project : parent
 * Package : com.silentgo.core.ioc.bean.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/29.
 */
public class CommonBeanHandler implements BeanHandler {

    private static final ArrayList anList = new ArrayList() {{
        add(Service.class);
        add(Component.class);
        add(Controller.class);
    }};

    @Override
    public <T extends Annotation> boolean hasHandle(T t, Class<?> clz) {
        return anList.contains(t.annotationType());
    }

    @Override
    public <T extends Annotation> void handle(T t, Class<?> clz, List<BeanDefinition> beanDefinitions) {
        BeanBuildKit.commonBuild(beanDefinitions, t, clz, true);
    }
}
