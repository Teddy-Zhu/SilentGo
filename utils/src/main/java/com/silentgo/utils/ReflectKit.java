package com.silentgo.utils;

import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;
import com.silentgo.utils.reflect.SGClass;
import com.silentgo.utils.reflect.SGClassParseKit;
import com.silentgo.utils.reflect.SGField;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project : SilentGo
 * Package : com.silentgo.utils
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by teddyzhu on 2017/1/5.
 */
public class ReflectKit {

    private static final Log LOGGER = LogFactory.get();

    private static final Map<Class<?>, SGClass> cachedSGClass = new HashMap<>();

    public static SGClass getSGClass(Class<?> clz) {
        Assert.isNotNull(clz, "class must be not null");
        SGClass sgClass = cachedSGClass.get(clz);
        if (sgClass != null) return sgClass;
        sgClass = SGClassParseKit.parse(clz);
        cachedSGClass.put(clz, sgClass);
        return sgClass;
    }


    public static Map<String, Object> beanToMap(Object object) {
        if (TypeConvertKit.isBaseType(object.getClass())) {
            throw new RuntimeException("convert bean failed,because the clz is base type");
        }

        SGClass sgClass = getSGClass(object.getClass());

        Map<String, Object> map = new HashMap<>();

        sgClass.getFieldMap().forEach((k, v) -> {
            if (v.hasGet()) {
                map.put(k, v.get(object));
            }
        });

        return map;
    }

    public static <O, T> T copyProperties(O origin, T target) {
        if (origin == null) return target;
        if (target == null) return null;
        SGClass sgClass = getSGClass(origin.getClass());
        SGClass targetClass = getSGClass(target.getClass());

        sgClass.getFieldMap().forEach((s, sgField) -> {
            SGField sgField1 = targetClass.getFieldMap().get(s);
            if (sgField.hasGet() && sgField1 != null && sgField1.hasSet()) {
                sgField1.set(target, sgField.get(origin));
            }
        });
        return target;
    }

    public static boolean contain(List<Annotation> annotations, Class<? extends Annotation> clz) {
        return annotations.stream().anyMatch(annotation -> annotation.annotationType().equals(clz));
    }

    public static Object init(Class<?> clz) {
        try {
            return clz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error(e);
            throw new RuntimeException(e);
        }
    }
}
