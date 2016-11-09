package com.silentgo.core.kit;

import com.silentgo.core.aop.support.AOPInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Field;

/**
 * Project : silentgo
 * com.silentgo.kit
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/19.
 */
public class CGLibKit {

    private static final Logger LOGGER = LoggerFactory.getLogger(CGLibKit.class);

    @SuppressWarnings("unchecked")
    public static <T> T getTarget(T target) {
        if (target.getClass().getName().contains("CGLIB$")) {
            try {
                Field field = target.getClass().getDeclaredField("CGLIB$CALLBACK_0");

                field.setAccessible(true);

                Object aoptarget = field.get(target);

                Field targetField = aoptarget.getClass().getDeclaredField("target");
                targetField.setAccessible(true);

                return (T) targetField.get(aoptarget);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } else
            LOGGER.error("The object [{}] is not Acc cglib-proxy class", target.getClass().getName());
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T Proxy(T target) {
        if (null == target) return null;
        Enhancer en = new Enhancer();
        en.setSuperclass(target.getClass());
        en.setCallback(new AOPInterceptor(target));
        return (T) en.create();
    }

}
