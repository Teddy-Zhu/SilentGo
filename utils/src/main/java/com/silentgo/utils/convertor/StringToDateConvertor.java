package com.silentgo.utils.convertor;


import com.silentgo.utils.inter.ITypeConvertor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.kit.typeconvert.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/27.
 */
public class StringToDateConvertor implements ITypeConvertor<String, Date> {

    private static final Map<String, ThreadLocal<SimpleDateFormat>> timeMap = new HashMap<String, ThreadLocal<SimpleDateFormat>>() {{
        put("yyyy-MM-dd HH:mm:ss", ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
    }};

    @Override
    public Date convert(String source, Object... objects) {
        try {
            if (objects.length > 0 && objects[0] instanceof String && timeMap.containsKey(objects[0])) {
                return timeMap.get(objects[0]).get().parse(source);
            }
            return timeMap.get("yyyy-MM-dd HH:mm:ss").get().parse(source);
        } catch (ParseException e) {
            return null;
        }
    }
}
