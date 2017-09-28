package com.silentgo.core.ioc.bean;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.annotation.Aspect;
import com.silentgo.core.aop.annotationintercept.annotation.CustomInterceptor;
import com.silentgo.core.aop.validator.annotation.Validator;
import com.silentgo.core.build.Factory;
import com.silentgo.core.config.SilentGoConfig;
import com.silentgo.core.exception.AppReleaseException;
import com.silentgo.core.exception.annotaion.ExceptionHandler;
import com.silentgo.core.ioc.annotation.Component;
import com.silentgo.core.ioc.annotation.Lazy;
import com.silentgo.core.ioc.annotation.Service;
import com.silentgo.core.ioc.bean.support.BeanBuildKit;
import com.silentgo.core.ioc.bean.support.BeanHandleFactory;
import com.silentgo.core.ioc.rbean.BeanFiledModel;
import com.silentgo.core.ioc.rbean.BeanInitModel;
import com.silentgo.core.ioc.rbean.BeanModel;
import com.silentgo.core.route.annotation.Controller;
import com.silentgo.utils.CollectionKit;
import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Project : silentgo
 * com.silentgo.core.ioc.bean
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by teddyzhu on 16/7/22.
 */
@Factory
public class SilentGoBeanFactory extends BeanFactory<BeanModel> {

    private static final Log LOGGER = LogFactory.get();

    private Map<String, BeanModel> beansMap = new HashMap<>();

    public SilentGoBeanFactory() {

    }

    @Override
    public void build(List<BeanModel> beans, SilentGoConfig config) {
        beans.forEach(beanDefinition -> {
            if (!CollectionKit.MapAdd(beansMap, beanDefinition.getBeanName(), beanDefinition) && config.isDevMode()) {
                LOGGER.debug("Bean [{}] has been registered.", beanDefinition.getBeanName());
            }
        });
        beans.forEach(this::depend);
    }

    public boolean contain(String name) {
        return beansMap.containsKey(name);
    }

    @SuppressWarnings("unchecked")
    public BeanModel getBean(String name) {
        return beansMap.getOrDefault(name, null);
    }

    @Override
    public BeanModel addBean(Class<?> beanDefinitionClass) {
        BeanInitModel beanInitModel = new BeanInitModel();
        beanInitModel.setBeanClass(beanDefinitionClass);
        beanInitModel.setCreateImmediately(beanDefinitionClass.getAnnotation(Lazy.class) == null);
        BeanModel beanDefinition = new BeanModel(beanInitModel);
        beansMap.put(beanDefinition.getBeanName(), beanDefinition);
        depend(beanDefinition);
        return beanDefinition;
    }

    @Override
    public BeanModel addBean(BeanModel beanDefinition) {
        beansMap.put(beanDefinition.getBeanName(), beanDefinition);
        depend(beanDefinition);
        return beanDefinition;
    }

    @Override
    public BeanModel addBean(Class<?> target, boolean isSingle, boolean needInject, boolean isLazy) {
        BeanInitModel beanInitModel = new BeanInitModel();
        beanInitModel.setBeanClass(target);
        beanInitModel.setNeedInject(needInject);
        beanInitModel.setSingle(isSingle);
        beanInitModel.setCreateImmediately(!isLazy);

        BeanModel beanDefinition = new BeanModel(beanInitModel);
        beansMap.put(beanDefinition.getBeanName(), beanDefinition);
        depend(beanDefinition);
        return beanDefinition;
    }

    @Override
    public BeanModel addBean(Object target, boolean isSingle, boolean needInject, boolean isLazy) {
        BeanInitModel beanInitModel = new BeanInitModel();
        beanInitModel.setOriginObject(target);
        beanInitModel.setNeedInject(needInject);
        beanInitModel.setSingle(isSingle);
        beanInitModel.setCreateImmediately(!isLazy);

        BeanModel beanDefinition = new BeanModel(beanInitModel);
        beansMap.put(beanDefinition.getBeanName(), beanDefinition);
        depend(beanDefinition);
        return beanDefinition;
    }

    @Override
    public Object getBeans() {
        return beansMap;
    }

    @Override
    public boolean destory(String name) {
        beansMap.remove(name);
        return true;
    }

    public void depend(BeanModel beanDefinition) {

        if (beanDefinition != null && beanDefinition.getFields() != null) {
            for (BeanFiledModel beanFiledModel : beanDefinition.getFields()) {
                String k = beanFiledModel.getBeanName();

                Field field = beanFiledModel.getField().getField();
                Class<?> type = field.getType();
                BeanModel bean;
                if (type.isInterface() && k.equals(type.getName())) {
                    try {
                        bean = beansMap.entrySet().stream().filter(keyset -> keyset.getValue().getInterfaceClass().getName().equals(k))
                                .findFirst().get().getValue();
                    } catch (NoSuchElementException ex) {
                        LOGGER.info("Can not find [{}] find implemented class bean", k);
                        return;
                    }

                } else {
                    bean = beansMap.get(k);
                }

                Lazy lazy = field.getAnnotation(Lazy.class);
                if (bean == null) {
                    if (lazy == null)
                        bean = addBean(type);
                    else
                        bean = addBean(type, true, false, true);
                }
                beanFiledModel.setBeanName(bean.getBeanName());

            }
        }
    }

    static ImmutableList<Class<? extends Annotation>> annatationList = ImmutableList.of(
            Service.class,
            Component.class,
            Controller.class,
            Aspect.class,
            ExceptionHandler.class,
            CustomInterceptor.class,
            Validator.class);

    static List<Class<? extends Annotation>> anList = Lists.newArrayList(Service.class,
            Component.class,
            Controller.class,
            Aspect.class,
            ExceptionHandler.class,
            CustomInterceptor.class,
            Validator.class);

    @Override
    public boolean initialize(SilentGo me) {
        List<BeanModel> beanDefinitions = new ArrayList<>();

        me.getFactory(BeanHandleFactory.class);

        for (Class<? extends Annotation> an : annatationList) {
            me.getAnnotationManager().getClasses(an).
                    forEach(aClass -> BeanBuildKit.getBeanHandlers().forEach(beanHandler -> {
                        Annotation annotation = aClass.getAnnotation(an);
                        if (beanHandler.preHandle(annotation, aClass)) {
                            beanHandler.handle(annotation, aClass, beanDefinitions);
                        }
                    }));
        }

        build(beanDefinitions, me.getConfig());

        return true;
    }

    @Override
    public boolean destroy(SilentGo me) throws AppReleaseException {
        return true;
    }

}
