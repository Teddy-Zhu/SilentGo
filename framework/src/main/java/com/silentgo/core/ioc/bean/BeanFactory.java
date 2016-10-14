package com.silentgo.core.ioc.bean;

import com.silentgo.core.config.SilentGoConfig;
import com.silentgo.core.support.BaseFactory;

import java.util.List;

/**
 * Project : silentgo
 * com.silentgo.core.ioc.bean
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/20.
 */
public abstract class BeanFactory<T extends BeanWrapper> extends BaseFactory {

    public abstract void build(List<T> beans, SilentGoConfig config);

    public abstract boolean contain(String name);

    public abstract T getBean(String name);

    public abstract T addBean(Class<?> tClass);

    public abstract T addBean(T beanDefinition);

    public abstract Object getBeans();

    public abstract boolean destory(String name);


}
