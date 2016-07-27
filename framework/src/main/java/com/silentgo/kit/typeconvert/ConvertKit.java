package com.silentgo.kit.typeconvert;

import com.silentgo.kit.typeconvert.support.StringToDateConvertor;
import com.silentgo.kit.typeconvert.support.StringToIntConvertor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.kit.typeconvert
 *
 * @author <Acc href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</Acc>
 *         <p>
 *         Created by teddyzhu on 16/7/26.
 */
public class ConvertKit {

    static {
        addConvert(StringToIntConvertor.class);
        addConvert(StringToDateConvertor.class);
    }

    static Map<Class<?>, Map> convertMap = new HashMap<>();

    public ConvertKit() {
    }

    public static boolean addConvert(ITypeConvertor convert) {
        Class<?> convertClass = convert.getClass();
        Type[] types = ((ParameterizedType) convertClass.getGenericSuperclass()).getActualTypeArguments();

        Class<?> source = types[0].getClass();

        Class<?> target = types[1].getClass();

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

    public static boolean addConvert(Class<?> convert) {
        try {
            return addConvert((ITypeConvertor) convert.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
}
