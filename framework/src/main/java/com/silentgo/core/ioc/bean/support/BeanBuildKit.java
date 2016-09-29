package com.silentgo.core.ioc.bean.support;

import com.silentgo.core.db.DaoInterceptor;
import com.silentgo.core.ioc.bean.BeanDefinition;
import com.silentgo.utils.CollectionKit;
import com.silentgo.utils.logger.Logger;
import com.silentgo.utils.logger.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
public class BeanBuildKit {

    private static final Logger LOGGER = LoggerFactory.getLog(BeanBuildKit.class);

    private static List<BeanHandler> beanHandlers = new ArrayList<>();

    public static List<BeanHandler> getBeanHandlers() {
        return beanHandlers;
    }

    public static boolean addBeanHander(BeanHandler beanHandler) {
        return CollectionKit.ListAdd(beanHandlers, beanHandler);
    }

    public static <T extends Annotation> void commonBuild(List<BeanDefinition> beanDefinitions, T annotation, Class<?> aClass, boolean inject) {

        try {
            Method method = aClass.getDeclaredMethod("value");
            beanDefinitions.add(new BeanDefinition(method.invoke(annotation).toString(), aClass, inject));
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            LOGGER.debug("no value , ignored");
        }
        beanDefinitions.add(new BeanDefinition(aClass, inject));
    }

    public static void buildBaseDaoInterface(List<BeanDefinition> beanDefinitions, Class<?> clz) {
        beanDefinitions.add(new BeanDefinition(clz, DaoInterceptor.proxy(clz), false, true));
    }
}
