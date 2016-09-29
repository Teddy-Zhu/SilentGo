package com.silentgo.core.ioc.bean.support;

import com.silentgo.core.SilentGo;
import com.silentgo.core.build.Factory;
import com.silentgo.core.exception.AppBuildException;
import com.silentgo.core.exception.AppReleaseException;
import com.silentgo.core.support.BaseFactory;

/**
 * Project : parent
 * Package : com.silentgo.core.ioc.bean.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/29.
 */
@Factory
public class BeanHandleFactory extends BaseFactory {
    @Override
    public boolean initialize(SilentGo me) throws AppBuildException {
        BeanBuildKit.addBeanHander(new CommonBeanHandler());
        BeanBuildKit.addBeanHander(new DaoBeanHandler());
        BeanBuildKit.addBeanHander(new NoInjectBeanHandler());

        return false;
    }

    @Override
    public boolean destroy(SilentGo me) throws AppReleaseException {
        return false;
    }
}
