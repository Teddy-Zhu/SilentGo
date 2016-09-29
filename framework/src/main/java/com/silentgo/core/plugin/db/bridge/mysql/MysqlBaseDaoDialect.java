package com.silentgo.core.plugin.db.bridge.mysql;

import com.silentgo.core.db.BaseDaoDialect;
import com.silentgo.core.db.BaseTableInfo;
import com.silentgo.core.db.TableModel;
import com.silentgo.utils.StringKit;
import com.silentgo.utils.logger.Logger;
import com.silentgo.utils.logger.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
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
public class MysqlBaseDaoDialect implements BaseDaoDialect {

    private static final Logger LOGGER = LoggerFactory.getLog(MysqlBaseDaoDialect.class);

    private static final Map<Class<? extends TableModel>, BeanInfo> beanMap = new ConcurrentHashMap<>();

    private static final Map<Class<? extends TableModel>, Map<String, PropertyDescriptor>> cachedPropMap = new ConcurrentHashMap<>();

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


        getCachedProps(table).forEach((k, propertyDescriptor) -> {
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
            sqlTool.whereEquals(table.getFullColumns().get(k)).appendParam(target);
        });

        return sqlTool;
    }

    @Override
    public <T extends TableModel> SQLTool insertByRow(BaseTableInfo table, T t) {

        SQLTool sqlTool = new SQLTool().insert(table.getTableName());

        getCachedProps(table).forEach((k, propertyDescriptor) -> {
            Object target = null;
            try {
                target = propertyDescriptor.getReadMethod().invoke(t);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            sqlTool.insert(k).appendParam(target);
        });
        return sqlTool;
    }

    @Override
    public <T extends TableModel> SQLTool insertByRows(BaseTableInfo table, List<T> t) {
        if (t.size() <= 0) return null;

        SQLTool sqlTool = insertByRow(table, t.get(0));

        for (int i = 1; i < t.size(); i++) {
            T cur = t.get(i);
            getCachedProps(table).forEach((k, propertyDescriptor) -> {
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

    @Override
    public <T extends TableModel> SQLTool updateByPrimaryKey(BaseTableInfo table, T t) {
        Map<String, PropertyDescriptor> propsMap = getCachedProps(table);
        SQLTool sqlTool = getUpdateByPrimerySQLTool(table, t, propsMap);

        propsMap.forEach((k, propertyDescriptor) -> {
            Object target = null;
            try {
                target = propertyDescriptor.getReadMethod().invoke(t);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                target = null;
            }
            sqlTool.set(k).appendParam(target);
        });
        return sqlTool;
    }

    @Override
    public <T extends TableModel> SQLTool updateByPrimaryKeyOptional(BaseTableInfo table, T t, List<String> columns) {
        Map<String, PropertyDescriptor> propsMap = getCachedProps(table);
        SQLTool sqlTool = getUpdateByPrimerySQLTool(table, t, propsMap);

        propsMap.forEach((k, propertyDescriptor) -> {
            if (!columns.contains(k)) return;
            Object target = null;
            try {
                target = propertyDescriptor.getReadMethod().invoke(t);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                target = null;
            }
            sqlTool.set(k).appendParam(target);
        });
        return sqlTool;
    }


    @Override
    public <T extends TableModel> SQLTool updateByPrimaryKeySelective(BaseTableInfo table, T t) {

        Map<String, PropertyDescriptor> propsMap = getCachedProps(table);
        SQLTool sqlTool = getUpdateByPrimerySQLTool(table, t, propsMap);

        propsMap.forEach((k, propertyDescriptor) -> {
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
            sqlTool.set(k).appendParam(target);
        });
        return sqlTool;
    }

    @Override
    public SQLTool deleteByPrimaryKey(BaseTableInfo table, Object id) {
        SQLTool sqlTool = new SQLTool();
        if (table.getPrimaryKeys().size() == 0) return sqlTool;
        sqlTool.delete(table.getTableName()).whereEquals(table.getPrimaryKeys().get(0)).appendParam(id);
        return sqlTool;
    }

    @Override
    public SQLTool deleteByPrimaryKeys(BaseTableInfo table, List<Object> ids) {
        SQLTool sqlTool = new SQLTool();
        if (table.getPrimaryKeys().size() == 0) return sqlTool;
        sqlTool.delete(table.getTableName())
                .whereIn(table.getPrimaryKeys().get(0), ids.size());
        ids.forEach(sqlTool::appendParam);
        return sqlTool;
    }


    private <T extends TableModel> SQLTool getUpdateByPrimerySQLTool(BaseTableInfo table, T t, Map<String, PropertyDescriptor> propsMap) {
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


    private Map<String, PropertyDescriptor> getCachedProps(BaseTableInfo tableInfo) {
        BeanInfo beanInfo = getBeanInfo(tableInfo.getClazz());
        if (cachedPropMap.containsKey(tableInfo.getClazz())) {
            return cachedPropMap.get(tableInfo.getClazz());
        } else {
            Map<String, PropertyDescriptor> propertyDescriptors = getProps(beanInfo, tableInfo);
            cachedPropMap.put(tableInfo.getClazz(), propertyDescriptors);
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
    private Map<String, PropertyDescriptor> getProps(BeanInfo beanInfo, BaseTableInfo tableInfo) {
        Map<String, PropertyDescriptor> propsMap = new HashMap<>();
        for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
            if (tableInfo.getColumnsMap().containsKey(propertyDescriptor.getName())) {
                propsMap.put(propertyDescriptor.getName(), propertyDescriptor);
            }
        }
        return propsMap;
    }
}
