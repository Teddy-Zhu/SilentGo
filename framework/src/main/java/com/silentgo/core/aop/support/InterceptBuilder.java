package com.silentgo.core.aop.support;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.Interceptor;
import com.silentgo.core.ioc.bean.BeanDefinition;
import com.silentgo.kit.CollectionKit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.aop.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/28.
 */
public class InterceptBuilder {

    private static Map<String, List<Interceptor>> classInterceptors = new HashMap<>();

    private InterceptBuilder() {
    }

    public static void Build(SilentGo me) {
        Map<String, BeanDefinition> beansMap = (Map<String, BeanDefinition>) me.getConfig().getBeanFactory().getBeans();

        beansMap.forEach((k, v) -> {
            CollectionKit.ListMapAdd(classInterceptors, v.getClassName(), me.getConfig().getInterceptors());
        });
    }

    public static Map<String, List<Interceptor>> getClassInterceptors() {
        return classInterceptors;
    }
}
