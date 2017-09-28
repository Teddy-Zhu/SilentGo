package com.silentgo.core.ioc.bean.support;

import com.silentgo.core.ioc.annotation.Lazy;
import com.silentgo.core.ioc.rbean.BeanInitModel;
import com.silentgo.core.ioc.rbean.BeanModel;
import com.silentgo.orm.base.DaoInterceptor;
import com.silentgo.utils.CollectionKit;
import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;

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
 * <p>
 * Created by teddyzhu on 16/9/29.
 */
public class BeanBuildKit {

    private static final Log LOGGER = LogFactory.get();
    private static List<BeanHandler> beanHandlers = new ArrayList<>();

    public static List<BeanHandler> getBeanHandlers() {
        return beanHandlers;
    }

    public static boolean addBeanHander(BeanHandler beanHandler) {
        return CollectionKit.ListAdd(beanHandlers, beanHandler);
    }

    public static void commonBuildNoValue(List<BeanModel> beanDefinitions, Class<?> aClass, boolean inject) {

        BeanInitModel beanInitModel = new BeanInitModel();
        beanInitModel.setBeanClass(aClass);
        beanInitModel.setCreateImmediately(true);
        beanInitModel.setNeedInject(inject);
        beanInitModel.setSingle(true);

        beanDefinitions.add(new BeanModel(beanInitModel));
    }

    public static <T extends Annotation> void commonBuild(List<BeanModel> beanDefinitions, T annotation, Class<?> aClass, boolean inject) {

        BeanInitModel beanInitModel = null;
        try {

            beanInitModel = new BeanInitModel();
            beanInitModel.setBeanClass(aClass);
            beanInitModel.setCreateImmediately(aClass.getAnnotation(Lazy.class) == null);
            beanInitModel.setNeedInject(inject);
            beanInitModel.setSingle(true);


            Method method = aClass.getDeclaredMethod("value");
            beanInitModel.setBeanName(method.invoke(annotation).toString());

            beanDefinitions.add(new BeanModel(beanInitModel));
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            LOGGER.debug("no value , ignored");
        }
        LOGGER.debug("reg class : {}", aClass);
        beanInitModel.setBeanName(null);
        beanDefinitions.add(new BeanModel(beanInitModel));
    }


    public static void buildBaseDaoInterface(List<BeanModel> beanDefinitions, Object target, Class<?> clz) {

        BeanInitModel beanInitModel = new BeanInitModel();
        beanInitModel.setBeanClass(clz);
        beanInitModel.setCreateImmediately(clz.getAnnotation(Lazy.class) == null);
        beanInitModel.setNeedInject(false);
        beanInitModel.setSingle(true);
        beanInitModel.setOriginObject(DaoInterceptor.proxy(clz));

        beanDefinitions.add(new BeanModel(beanInitModel));
    }
}
