package com.silentgo.core.ioc.bean;

import net.sf.cglib.reflect.FastClass;

/**
 * Project : silentgo
 * com.silentgo.core.ioc.bean
 *
 * @author    <Acc href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</Acc>
 * <p>
 * Created by teddyzhu on 16/7/20.
 */
public abstract class BeanWrapper {

    public abstract Object getBean();
    public abstract FastClass getBeanClass();
    public abstract String getBeanName();

}
