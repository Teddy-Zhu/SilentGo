package com.silentgo.core.aop.aspect;

import com.silentgo.core.SilentGo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Project : silentgo
 * com.silentgo.core.aop
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/29.
 */
public class AspectMethod {
    private String rule;

    private boolean regex;

    private Method method;

    private Class<?> clz;

    public Object invoke(Object... args) throws IllegalAccessException, InvocationTargetException {
        SilentGo me = SilentGo.me();
        Object target = me.getFactory(me.getConfig().getBeanClass()).getBean(clz.getName());
        return method.invoke(target, args);
    }

    public AspectMethod(String rule, boolean regex, Method method, Class<?> clz) {
        this.rule = rule;
        this.regex = regex;
        this.method = method;
        this.clz = clz;
    }

    public String getRule() {
        return rule;
    }

    public boolean isRegex() {
        return regex;
    }

    public Method getMethod() {
        return method;
    }

}
