package com.silentgo.core.typeconvert;


import com.silentgo.core.SilentGo;
import com.silentgo.core.build.Factory;
import com.silentgo.core.exception.AppBuildException;
import com.silentgo.core.exception.AppReleaseException;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.core.typeconvert.annotation.Convertor;
import com.silentgo.core.typeconvert.support.CommonConvertor;
import com.silentgo.utils.ClassKit;
import com.silentgo.utils.TypeConvertKit;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.kit.typeconvert
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/26.
 */
@Factory
public class ConvertKit extends BaseFactory {


    static Map<Class<?>, Map<Class<?>, ITypeConvertor>> convertMap = new HashMap<>();

    public ConvertKit() {
    }

    public ITypeConvertor getTypeConvert(Class<?> source, Class<?> target) {
        return getTypeConvert(source, target, new CommonConvertor());
    }

    public ITypeConvertor getTypeConvert(Class<?> source, Class<?> target, ITypeConvertor defaultVal) {
        if (target.isPrimitive()) {
            target = TypeConvertKit.getConvertType(target);
        }

        Map<Class<?>, ITypeConvertor> convertorMap = convertMap.get(source);
        return convertorMap == null ? defaultVal : convertorMap.getOrDefault(target, defaultVal);
    }

    public boolean addConvert(ITypeConvertor convert) {
        Class<?> convertClass = convert.getClass();
        Type[] types = ClassKit.getGenericClass(convertClass);

        Class<?> source = (Class<?>) types[0];

        Class<?> target = (Class<?>) types[1];

        Map converts = convertMap.get(source);
        if (converts == null) {
            converts = new HashMap<Class<?>, ITypeConvertor>() {{
                put(target, convert);
            }};
            convertMap.put(source, converts);
        } else {
            if (!converts.containsKey(target))
                converts.put(target, convert);
        }
        return true;
    }

    public boolean addConvert(Class<?> convert) {
        try {
            return addConvert((ITypeConvertor) convert.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean initialize(SilentGo me) throws AppBuildException {
        me.getAnnotationManager().getClasses(Convertor.class)
                .forEach(aClass -> {
                    if (ITypeConvertor.class.isAssignableFrom(aClass))
                        addConvert(aClass);
                });
        return true;
    }

    @Override
    public boolean destroy(SilentGo me) throws AppReleaseException {
        return false;
    }
}
