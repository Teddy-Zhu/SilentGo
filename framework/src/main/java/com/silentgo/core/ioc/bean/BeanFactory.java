package com.silentgo.core.ioc.bean;

import com.silentgo.config.SilentGoConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.ioc.bean
 *
 * @author <Acc href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</Acc>
 *         <p>
 *         Created by teddyzhu on 16/7/20.
 */
public abstract class BeanFactory<T extends BeanWrapper> {

    public abstract void build(List<T> beans, SilentGoConfig config);

    public abstract boolean has(String name);

    public abstract T getBean(String name);

    public abstract T addBean(Class<?> tClass);

}
