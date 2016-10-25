package com.silentgo.core.ioc.bean;

import com.silentgo.core.SilentGo;
import com.silentgo.core.config.Const;
import com.silentgo.core.ioc.annotation.Component;
import com.silentgo.core.ioc.annotation.Inject;
import com.silentgo.core.ioc.annotation.Service;
import com.silentgo.core.kit.CGLibKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.ioc.bean
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/20.
 */
public class BeanDefinition extends BeanWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanDefinition.class);

    String beanName;

    Class<?> interfaceClass;

    Class<?> clz;

    Class<?> fastClass;

    Object target;

    Object proxyTarget;

    Map<String, FieldBean> fieldBeans;

    boolean lazy = false;

    boolean injectComplete = false;

    boolean isSingle = false;

    boolean needInject = false;

    public boolean isInjectComplete() {
        return injectComplete;
    }

    public void setInjectComplete(boolean injectComplete) {
        this.injectComplete = injectComplete;
    }

    public BeanDefinition(Class<?> clz, boolean inject, boolean isLazy) {
        Create(clz.getName(), clz, inject, isLazy);
    }

    public BeanDefinition(Class<?> clz, Object target, boolean inject, boolean isSingle, boolean isLazy) {
        this.isSingle = isSingle;
        Create(clz.getName(), clz, inject, target, isLazy);
    }

    public BeanDefinition(String beanName, Class<?> clz, boolean inject, boolean isLazy) {
        Create(beanName, clz, inject, isLazy);
    }


    public BeanDefinition(Class<?> clz, boolean isLazy) {
        Create(clz.getName(), clz, true, isLazy);
    }

    public BeanDefinition(String beanName, Class<?> clz, boolean isLazy) {
        Create(beanName, clz, true, isLazy);
    }

    private void Create(String beanName, Class<?> clz, boolean needInject, Object target, boolean isLazy) {
        this.needInject = needInject;
        this.beanName = beanName;
        this.clz = clz;
        this.lazy = isLazy;
        this.target = target;
        this.fastClass = clz;
        interfaceClass = clz.isInterface() ? clz : clz.getInterfaces().length > 0 ? clz.getInterfaces()[0] : clz;
        if (needInject)
            proxyTarget = CGLibKit.Proxy(target);
        else
            proxyTarget = target;

        fieldBeans = new HashMap<>();
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            Inject inject = field.getAnnotation(Inject.class);
            //filter no annotation class
            if (inject != null) {
                if (Const.DEFAULT_NONE.equals(inject.value()))
                    fieldBeans.put(field.getType().getName(), new FieldBean(field));
                else
                    fieldBeans.put(inject.value(), new FieldBean(field));
            }
        }
    }

    private void Create(String beanName, Class<?> clz, boolean needInject, boolean isLazy) {
        Object obj = null;
        try {
            obj = clz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        Create(beanName, clz, needInject, obj, isLazy);
    }

    private boolean containAnnotation(Field field) {
        Class<?> clz = field.getType();
        if (clz.getAnnotation(com.silentgo.core.route.annotation.Controller.class) != null)
            return true;
        if (clz.getAnnotation(Component.class) != null)
            return true;
        if (clz.getAnnotation(Service.class) != null)
            return true;
        return false;
    }

    public Map<String, FieldBean> getFieldBeans() {
        return fieldBeans;
    }

    @Override
    public Object getObject() {
        SilentGoBeanFactory beanFactory = SilentGo.me().getFactory(SilentGoBeanFactory.class);
        if (!injectComplete) {
            beanFactory.depend(this);
        }
        if (!isSingle) {
            try {
                target = clz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }

            if (needInject)
                proxyTarget = CGLibKit.Proxy(target);
            else
                proxyTarget = target;

            fieldBeans.forEach((k, v) -> {
                LOGGER.debug("get bean class : {}", k);
                Object object = beanFactory.getBean(v.getBeanName()).getObject();
                v.setValue(target, object);
                v.setValue(proxyTarget, object);
            });
        }
        return proxyTarget;
    }


    @Override
    public Class<?> getBeanClass() {
        return fastClass;
    }

    @Override
    public String getBeanName() {
        return beanName;
    }

    @Override
    public Class<?> getInterfaceClass() {
        return interfaceClass;
    }

    public Object getTarget() {
        return target;
    }


    public String getClassName() {
        return clz.getName();
    }

    public boolean isLazy() {
        return lazy;
    }

}
