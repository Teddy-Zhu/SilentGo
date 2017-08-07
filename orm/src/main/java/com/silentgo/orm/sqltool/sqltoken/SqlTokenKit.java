package com.silentgo.orm.sqltool.sqltoken;

import com.silentgo.utils.ReflectKit;
import com.silentgo.utils.reflect.SGClass;
import com.silentgo.utils.reflect.SGField;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

public class SqlTokenKit {

    public static Object parseObject(String name, Object object) {
        Object current = null;
        if (object == null) return null;
        if (object instanceof Map) {
            current = ((Map) object).get(name);
        } else if (object instanceof List) {
            int i = Integer.valueOf(name);
            current = ((List) object).get(i);

        } else if (object.getClass().isArray()) {
            int i = Integer.valueOf(name);
            current = Array.get(object, i);
        } else {
            SGClass sgClass = ReflectKit.getSGClass(object.getClass());
            SGField sgField = sgClass.getField(name);
            if (sgField != null) {
                current = sgField.get(object);
            }
        }
        return current;
    }

}
