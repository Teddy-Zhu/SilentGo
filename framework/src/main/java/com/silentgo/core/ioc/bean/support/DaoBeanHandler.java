package com.silentgo.core.ioc.bean.support;

import com.silentgo.core.db.BaseDao;
import com.silentgo.core.ioc.bean.BeanDefinition;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Project : parent
 * Package : com.silentgo.core.ioc.bean.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/29.
 */
public class DaoBeanHandler implements BeanHandler {
    @Override
    public <T extends Annotation> boolean hasHandle(T t, Class<?> clz) {
        return clz.isInterface() && BaseDao.class.isAssignableFrom(clz);
    }

    @Override
    public <T extends Annotation> void handle(T t, Class<?> clz, List<BeanDefinition> beanDefinitions) {
        BeanBuildKit.buildBaseDaoInterface(beanDefinitions, clz);
    }
}
