package com.silentgo.core.ioc.bean;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.annotation.Aspect;
import com.silentgo.core.build.Factory;
import com.silentgo.core.config.SilentGoConfig;
import com.silentgo.core.exception.AppReleaseException;
import com.silentgo.core.exception.annotaion.ExceptionHandler;
import com.silentgo.core.ioc.annotation.Component;
import com.silentgo.core.ioc.annotation.Service;
import com.silentgo.core.ioc.bean.support.BeanBuildKit;
import com.silentgo.core.ioc.bean.support.BeanHandleFactory;
import com.silentgo.core.route.annotation.Controller;
import com.silentgo.utils.CollectionKit;
import com.silentgo.utils.StringKit;
import com.silentgo.utils.logger.Logger;
import com.silentgo.utils.logger.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Project : silentgo
 * com.silentgo.core.ioc.bean
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/22.
 */
@Factory
public class SilentGoBeanFactory extends BeanFactory<BeanDefinition> {

    private static final Logger LOGGER = LoggerFactory.getLog(SilentGoBeanFactory.class);

    private Map<String, BeanDefinition> beansMap = new HashMap<>();

    public SilentGoBeanFactory() {

    }

    @Override
    public void build(List<BeanDefinition> beans, SilentGoConfig config) {
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
    public BeanDefinition getBean(String name) {
        return beansMap.getOrDefault(name, null);
    }

    @Override
    public BeanDefinition addBean(Class<?> beanDefinitionClass) {
        BeanDefinition beanDefinition = new BeanDefinition(beanDefinitionClass);
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

    private void depend(BeanDefinition beanDefinition) {
        if (beanDefinition.isInjectComplete()) return;
        beanDefinition.getFieldBeans().forEach((k, v) -> {
            Field field = v.getField();
            Class<?> type = field.getType();
            BeanDefinition bean = null;
            if (type.isInterface() && k.equals(type.getName())) {
                try {
                    bean = beansMap.entrySet().stream().filter(keyset -> keyset.getValue().getInterfaceClass().getName().equals(k))
                            .findFirst().get().getValue();
                } catch (NoSuchElementException ex) {
                    LOGGER.error("Can not find [{}] find implemented class bean", k);
                    return;
                }

            } else {
                bean = beansMap.get(k);
            }
            if (bean == null) {
                bean = addBean(type);
            }
            v.setBeanName(bean.getBeanName());
            try {
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(),
                        beanDefinition.getSourceClass(),
                        "get" + StringKit.firstToUpper(field.getName()),
                        "set" + StringKit.firstToUpper(field.getName())
                );

                Method method = pd.getWriteMethod();
                if (method != null) {
                    v.setHasSet(true);
                    v.setSetMethod(method);
                    method.setAccessible(true);
                    method.invoke(beanDefinition.getTarget(), bean);
                }
            } catch (IllegalAccessException | IntrospectionException | InvocationTargetException e) {
                field.setAccessible(true);
                try {
                    field.set(beanDefinition.getTarget(), bean.getBean());
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                }
                LOGGER.debug("Field {} Can not find getter and setter in Class {}", field.getName(), bean.getSourceClass());
            }
        });
        beanDefinition.setInjectComplete(true);
    }

    private static ArrayList<Class<? extends Annotation>> anList = new ArrayList() {{
        add(Service.class);
        add(Component.class);
        add(Controller.class);
        add(Aspect.class);
        add(ExceptionHandler.class);
    }};

    @Override
    public boolean initialize(SilentGo me) {
        List<BeanDefinition> beanDefinitions = new ArrayList<>();

        me.getFactory(BeanHandleFactory.class);

        anList.forEach(an -> me.getAnnotationManager().getClasses(an).forEach(aClass -> BeanBuildKit.getBeanHandlers().forEach(beanHandler -> {
            Annotation annotation = aClass.getAnnotation(an);
            if (beanHandler.hasHandle(annotation, aClass)) {
                beanHandler.handle(annotation, aClass, beanDefinitions);
            }
        })));

        build(beanDefinitions, me.getConfig());

        return true;
    }

    @Override
    public boolean destroy(SilentGo me) throws AppReleaseException {
        return true;
    }

}
