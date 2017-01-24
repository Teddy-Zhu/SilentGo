package com.silentgo.core.ioc.bean;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.support.MethodAOPFactory;
import com.silentgo.core.config.Const;
import com.silentgo.core.ioc.annotation.Component;
import com.silentgo.core.ioc.annotation.Inject;
import com.silentgo.core.ioc.annotation.Service;
import com.silentgo.core.kit.CGLibKit;
import com.silentgo.utils.ReflectKit;
import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;
import com.silentgo.utils.reflect.SGClass;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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

    private static final Log LOGGER = LogFactory.get();
    String beanName;

    Class<?> interfaceClass;

    Class<?> clz;

    Object target;

    Object proxyTarget;

    Map<String, FieldBean> fieldBeans;

    boolean lazy = false;

    boolean injectComplete = false;

    boolean injectField = false;
    boolean isSingle = true;

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
        MethodAOPFactory methodAOPFactory = SilentGo.me().getFactory(MethodAOPFactory.class);

        methodAOPFactory.buildMethodAdviser(clz);
        this.needInject = needInject;
        this.beanName = beanName;
        this.clz = clz;
        this.lazy = isLazy;
        this.target = target;
        interfaceClass = clz.isInterface() ? clz : clz.getInterfaces().length > 0 ? clz.getInterfaces()[0] : clz;
        if (needInject)
            proxyTarget = CGLibKit.Proxy(target);
        else {
            proxyTarget = target;
        }
        fieldBeans = new HashMap<>();

        SGClass sgClass = ReflectKit.getSGClass(clz);

        sgClass.getFieldMap().forEach((name, field) -> {
            Inject inject = (Inject) field.getAnnotationMap().get(Inject.class);
            //filter no annotation class
            if (inject != null) {
                if (Const.DEFAULT_NONE.equals(inject.value()))
                    fieldBeans.put(field.getType().getName(), new FieldBean(field, field.getType().getName()));
                else
                    fieldBeans.put(inject.value(), new FieldBean(field, inject.value()));
            }
        });
    }

    private void Create(String beanName, Class<?> clz, boolean needInject, boolean isLazy) {
        Object obj = null;
        try {
            obj = clz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            LOGGER.info("clz {} instance error", clz.getName());
        }
        Create(beanName, clz, needInject, obj, isLazy);
    }

    @Override
    public Object getObject() {

        SilentGo me = SilentGo.me();
        SilentGoBeanFactory beanFactory = me.getFactory(SilentGoBeanFactory.class);
        if (!injectComplete) {
            lazy = false;
            beanFactory.depend(this);
        }
        if (!isSingle) {
            try {
                target = clz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            MethodAOPFactory methodAOPFactory = me.getFactory(MethodAOPFactory.class);
            if (needInject) {
                if (!methodAOPFactory.hasInitClass(clz))
                    methodAOPFactory.buildMethodAdviser(clz);
                if (methodAOPFactory.hasInterceptors(clz)) {
                    proxyTarget = CGLibKit.Proxy(target);
                } else {
                    proxyTarget = target;
                }
            } else
                proxyTarget = target;
        } else {
            if (injectField)
                return proxyTarget;
        }

        fieldBeans.forEach((k, v) -> {
            if (v.getValue(target) != null) return;
            LOGGER.debug("get bean class : {}", k);
            BeanDefinition beanDefinition = beanFactory.getBean(v.getBeanName());
            Object object = beanDefinition.getObject();
            v.setValue(target, object);
            v.setValue(proxyTarget, object);
        });
        injectField = true;
        return proxyTarget;
    }


    @Override
    public Class<?> getBeanClass() {
        return clz;
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

    public Map<String, FieldBean> getFieldBeans() {
        return fieldBeans;
    }
}
