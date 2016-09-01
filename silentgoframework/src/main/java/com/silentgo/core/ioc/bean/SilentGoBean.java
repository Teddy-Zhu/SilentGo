package com.silentgo.core.ioc.bean;

import com.silentgo.core.config.Const;
import com.silentgo.core.config.SilentGoConfig;
import com.silentgo.kit.CollectionKit;
import com.silentgo.kit.StringKit;
import com.silentgo.kit.logger.Logger;
import com.silentgo.kit.logger.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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
}
