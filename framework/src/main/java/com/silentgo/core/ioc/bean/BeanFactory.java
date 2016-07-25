package com.silentgo.core.ioc.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.ioc.bean
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/20.
 */
public abstract class BeanFactory<T extends BeanWrapper> {

    public abstract boolean has(String name);

    public abstract T getBean(String name);

    public abstract T addBean(Class<?> tClass);

}
