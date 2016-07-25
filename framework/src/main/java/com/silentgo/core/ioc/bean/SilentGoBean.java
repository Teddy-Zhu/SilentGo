package com.silentgo.core.ioc.bean;

import com.silentgo.config.SilentGoConfig;
import com.silentgo.logger.Logger;
import com.silentgo.logger.LoggerFactory;
import sun.dc.pr.PRError;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public SilentGoBean(List<BeanDefinition> beans, SilentGoConfig config) {
        beans.forEach(beanDefinition -> {
            if (!beansMap.containsKey(beanDefinition.getBeanName())) {
                beansMap.put(beanDefinition.getBeanName(), beanDefinition);
            } else {
                if (config.isDevMode()) {
                    LOGGER.debug("Bean [{}] has been registered.", beanDefinition.getBeanName());
                }
            }
        });
        beans.forEach(this::depend);
    }

    private void depend(BeanDefinition beanDefinition) {
        if (beanDefinition.isInjectComplete()) return;
        beanDefinition.getFieldBeans().forEach((k, v) -> {
            BeanDefinition bean = beansMap.get(k);
            if (bean == null) {
                bean = addBean(v.getType());
            }
            try {
                PropertyDescriptor pd = new PropertyDescriptor(v.getName(), beanDefinition.getBeanClass());

                Method method = pd.getWriteMethod();
                if (method != null) {
                    method.setAccessible(true);
                    method.invoke(beanDefinition.getTarget(), bean);
                } else {
                    v.setAccessible(true);
                    v.set(beanDefinition.getTarget(), bean);
                }
            } catch (IllegalAccessException | IntrospectionException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
        beanDefinition.setInjectComplete(true);
    }

    public boolean has(String name) {
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

}
