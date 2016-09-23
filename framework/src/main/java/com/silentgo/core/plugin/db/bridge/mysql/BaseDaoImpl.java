package com.silentgo.core.plugin.db.bridge.mysql;

import com.silentgo.core.plugin.db.SQLTool;
import com.silentgo.core.plugin.db.bridge.BaseTableInfo;
import com.silentgo.core.plugin.db.bridge.TableModel;
import com.silentgo.utils.StringKit;
import com.silentgo.utils.logger.Logger;
import com.silentgo.utils.logger.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Project : silentgo
 * com.silentgo.core.plugin.db.bridge.mysql
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/22.
 */
public class BaseDaoImpl implements IBaseDao {

    private static final Logger LOGGER = LoggerFactory.getLog(BaseDaoImpl.class);

    private static final Map<Class<? extends TableModel>, BeanInfo> beanMap = new ConcurrentHashMap<>();

    private static final Map<Class<? extends TableModel>, List<PropertyDescriptor>> cachedPropMap = new ConcurrentHashMap<>();

    @Override
    public SQLTool queryByPrimaryKey(BaseTableInfo table, Object id) {
        if (table.getPrimaryKeys().size() == 0) {
            LOGGER.debug("table {} can not find primary key", table.getTableName());
            throw new RuntimeException("the table did not has primary key");
        }
        SQLTool sqlTool = new SQLTool();
        sqlTool.select(table.getTableName(), table.getFullColumns().values())
                .whereEquals(table.getFullColumns().get(table.getPrimaryKeys().get(0)))
                .appendParam(id);
        return sqlTool;
    }

    @Override
    public SQLTool queryByPrimaryKeys(BaseTableInfo table, List<Object> ids) {
        if (table.getPrimaryKeys().size() == 0) {
            LOGGER.debug("table {} can not find primary key", table.getTableName());
            throw new RuntimeException("the table did not has primary key");
        }

        SQLTool sqlTool = new SQLTool();
        sqlTool.select(table.getTableName(), table.getFullColumns().values())
                .whereIn(table.getFullColumns().get(table.getPrimaryKeys().get(0)), ids.size());
        ids.forEach(sqlTool::appendParam);
        return sqlTool;
    }

    @Override
    public <T extends TableModel> SQLTool queryByModelSelective(BaseTableInfo table, T t) {

        SQLTool sqlTool = new SQLTool();
        sqlTool.select(table.getTableName(), table.getFullColumns().values());


        getCachedProps(t.getClass(), table).forEach(propertyDescriptor -> {
            Object target = null;
            try {
                target = propertyDescriptor.getReadMethod().invoke(t);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            if (target == null) {
                return;
            }
            if (target instanceof String && StringKit.isBlank(target.toString())) {
                return;
            }
            sqlTool.whereEquals(table.getFullColumns().get(propertyDescriptor.getName())).appendParam(target);
        });

        return sqlTool;
    }

    @Override
    public <T extends TableModel> SQLTool insertByRow(BaseTableInfo table, T t) {

        SQLTool sqlTool = new SQLTool().insert(table.getTableName());

        getCachedProps(t.getClass(), table).forEach(propertyDescriptor -> {
            Object target = null;
            try {
                target = propertyDescriptor.getReadMethod().invoke(t);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            sqlTool.insert(propertyDescriptor.getName()).appendParam(target);
        });
        return sqlTool;
    }

    @Override
    public <T extends TableModel> SQLTool insertByRows(BaseTableInfo table, List<T> t) {
        if (t.size() <= 0) return null;

        SQLTool sqlTool = insertByRow(table, t.get(0));

        for (int i = 1; i < t.size(); i++) {
            T cur = t.get(i);
            getCachedProps(cur.getClass(), table).forEach(propertyDescriptor -> {
                Object target = null;
                try {
                    target = propertyDescriptor.getReadMethod().invoke(cur);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                sqlTool.appendParam(target);
            });
        }
        return sqlTool;
    }





    private BeanInfo getBeanInfo(Class<? extends TableModel> t) {
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

    private List<PropertyDescriptor> getCachedProps(Class<? extends TableModel> clz, BaseTableInfo tableInfo) {
        BeanInfo beanInfo = getBeanInfo(clz);
        if (cachedPropMap.containsKey(clz)) {
            return cachedPropMap.get(clz);
        } else {
            List<PropertyDescriptor> propertyDescriptors = getProps(beanInfo, tableInfo);
            cachedPropMap.put(clz, propertyDescriptors);
            return propertyDescriptors;
        }
    }

    /**
     * get prop info from bean
     *
     * @param beanInfo
     * @param tableInfo
     * @return
     */
    private List<PropertyDescriptor> getProps(BeanInfo beanInfo, BaseTableInfo tableInfo) {
        List<PropertyDescriptor> props = new ArrayList<>();
        for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
            if (tableInfo.getColumnsMap().containsKey(propertyDescriptor.getName())) {
                props.add(propertyDescriptor);
            }
        }
        return props;
    }
}
