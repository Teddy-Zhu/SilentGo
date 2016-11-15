package com.silentgo.orm.kit;

import com.silentgo.orm.base.BaseTableInfo;
import com.silentgo.orm.base.TableModel;
import com.silentgo.orm.base.SQLTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Project : SilentGo
 * Package : com.silentgo.core.plugin.db.util
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/10.
 */
public class PropertyKit {


    public static final Logger LOGGER = LoggerFactory.getLogger(PropertyKit.class);

    private static final Map<Class<? extends TableModel>, BeanInfo> beanMap = new ConcurrentHashMap<>();

    private static final Map<Class<? extends TableModel>, Map<String, PropertyDescriptor>> cachedPropMap = new ConcurrentHashMap<>();


    public static <T extends TableModel> SQLTool getUpdateByPrimerySQLTool(BaseTableInfo table, T t, Map<String, PropertyDescriptor> propsMap) {
        SQLTool sqlTool = new SQLTool();
        sqlTool.update(table.getTableName());

        table.getPrimaryKeys().forEach(key -> {

            PropertyDescriptor propertyDescriptor = propsMap.get(key);
            Object target;
            try {
                target = propertyDescriptor.getReadMethod().invoke(t);
            } catch (IllegalAccessException | InvocationTargetException e) {
                target = null;
            }
            sqlTool.whereEquals(key).appendParam(target);
        });
        return sqlTool;
    }


    public static  BeanInfo getBeanInfo(Class<? extends TableModel> t) {
        BeanInfo beanInfo = null;
        if (beanMap.containsKey(t)) {
            beanInfo = beanMap.get(t);
        } else {
            try {
                beanInfo = Introspector.getBeanInfo(t);
                beanMap.put(t, beanInfo);
            } catch (IntrospectionException e) {
                LOGGER.debug("create bean :{} failed", t);
                e.printStackTrace();
            }
        }
        return beanInfo;
    }


    public static  Map<String, PropertyDescriptor> getCachedProps(BaseTableInfo tableInfo) {
        BeanInfo beanInfo = getBeanInfo(tableInfo.getClazz());
        if (cachedPropMap.containsKey(tableInfo.getClazz())) {
            return cachedPropMap.get(tableInfo.getClazz());
        } else {
            Map<String, PropertyDescriptor> propertyDescriptors = getProps(beanInfo, tableInfo);
            cachedPropMap.put(tableInfo.getClazz(), propertyDescriptors);
            return propertyDescriptors;
        }
    }

    public static  Map<String, PropertyDescriptor> getProps(BeanInfo beanInfo, BaseTableInfo tableInfo) {
        Map<String, PropertyDescriptor> propsMap = new HashMap<>();
        for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
            if (tableInfo.getColumnInfo().containsKey(propertyDescriptor.getName())) {
                propsMap.put(propertyDescriptor.getName(), propertyDescriptor);
            }
        }
        return propsMap;
    }
}
