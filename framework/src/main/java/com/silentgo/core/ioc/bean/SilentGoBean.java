package com.silentgo.core.ioc.bean;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.annotation.Aspect;
import com.silentgo.core.config.SilentGoConfig;
import com.silentgo.core.exception.AppReleaseException;
import com.silentgo.core.exception.annotaion.ExceptionHandler;
import com.silentgo.core.ioc.annotation.Component;
import com.silentgo.core.ioc.annotation.Service;
import com.silentgo.core.plugin.db.bridge.BaseDao;
import com.silentgo.core.plugin.db.bridge.DaoInterceptor;
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
public class SilentGoBean extends BeanFactory<BeanDefinition> {

    private static final Logger LOGGER = LoggerFactory.getLog(SilentGoBean.class);

    private Map<String, BeanDefinition> beansMap = new HashMap<>();

    public SilentGoBean() {

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
                        "get" + StringKit.FirstToUpper(field.getName()),
                        "set" + StringKit.FirstToUpper(field.getName())
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

    @Override
    public boolean initialize(SilentGo me) {
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
        me.getConfig().addFactory(beanFactory);
        beanFactory.build(beanDefinitions, me.getConfig());

        return true;
    }

    @Override
    public boolean destroy(SilentGo me) throws AppReleaseException {
        return false;
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
                LOGGER.debug("no value , ignored");
            }
            beanDefinitions.add(new BeanDefinition(aClass, inject));
        });

    }
}
