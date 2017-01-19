package com.silentgo.orm.dialect;

import com.silentgo.orm.base.*;
import com.silentgo.orm.kit.PropertyKit;
import com.silentgo.utils.StringKit;
import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.plugin.db.bridge.mysql
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/22.
 */
public class MysqlBaseDaoDialect implements BaseDaoDialect {

    private static final Log LOGGER = LogFactory.get();

    @Override
    public SQLTool queryByPrimaryKey(BaseTableInfo table, Object id) {
        if (table.getPrimaryKeys().size() == 0) {
            LOGGER.debug("table {} can not find primary key", table.getTableName());
            throw new RuntimeException("the table did not has primary key");
        }
        SQLTool sqlTool = new SQLTool();
        sqlTool.select(table.getTableName(), table.get("*").getSelectFullName())
                .whereEquals(table.get(table.getPrimaryKeys().get(0)).getFullName())
                .appendParam(id);
        return sqlTool;
    }

    @Override
    public SQLTool queryByPrimaryKeys(BaseTableInfo table, Collection<Object> ids) {
        if (table.getPrimaryKeys().size() == 0) {
            LOGGER.debug("table {} can not find primary key", table.getTableName());
            throw new RuntimeException("the table did not has primary key");
        }

        SQLTool sqlTool = new SQLTool();
        sqlTool.select(table.getTableName(), table.get("*").getSelectFullName())
                .whereIn(table.get(table.getPrimaryKeys().get(0)).getFullName(), ids.size());
        ids.forEach(sqlTool::appendParam);
        return sqlTool;
    }

    @Override
    public <T extends TableModel> SQLTool queryByModelSelective(BaseTableInfo table, T t) {

        SQLTool sqlTool = new SQLTool();
        sqlTool.select(table.getTableName(), table.get("*").getSelectFullName());


        PropertyKit.getCachedProps(table).forEach((k, propertyDescriptor) -> {
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
            sqlTool.whereEquals(table.get(k).getFullName()).appendParam(target);
        });

        return sqlTool;
    }

    @Override
    public <T extends TableModel> SQLTool insertByRow(BaseTableInfo table, T t) {

        SQLTool sqlTool = new SQLTool().insert(table.getTableName());

        PropertyKit.getCachedProps(table).forEach((k, propertyDescriptor) -> {
            Object target = null;
            try {
                target = propertyDescriptor.getReadMethod().invoke(t);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            Column column = table.get(k);
            if (target == null) {
                if (column.isAutoIncrement()) return;
                if (column.isNullable()) {
                    sqlTool.insertCol(column.getFullName()).appendParam(target);
                } else {
                    if (column.isHasDefault()) {
                        //ignore
                    } else {
                        throw new RuntimeException("column: " + column.getColumnName() + " can not be null");
                    }
                }
            } else {
                sqlTool.insertCol(column.getFullName()).appendParam(target);
            }
        });
        return sqlTool;
    }

    @Override
    public <T extends TableModel> SQLTool insertByRows(BaseTableInfo table, Collection<T> t) {
        if (t.size() <= 0) return null;

        Iterator iterator = t.iterator();

        SQLTool sqlTool = insertByRow(table, (T) iterator.next());

        while (iterator.hasNext()) {
            T cur = (T) iterator.next();
            PropertyKit.getCachedProps(table).forEach((k, propertyDescriptor) -> {
                Object target = null;
                try {
                    target = propertyDescriptor.getReadMethod().invoke(cur);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                Column column = table.get(k);
                if (target == null) {
                    if (column.isAutoIncrement()) return;
                    if (column.isNullable()) {
                        sqlTool.appendParam((Object) null);
                    } else {
                        if (column.isHasDefault()) {
                            //ignore
                        } else {
                            throw new RuntimeException("column: " + column.getColumnName() + " can not be null");
                        }
                    }
                } else {
                    sqlTool.appendParam(target);
                }
            });
        }
        return sqlTool;
    }

    @Override
    public <T extends TableModel> SQLTool updateByPrimaryKey(BaseTableInfo table, T t) {
        Map<String, PropertyDescriptor> propsMap = PropertyKit.getCachedProps(table);
        SQLTool sqlTool = getUpdateByPrimerySQLTool(table, t, propsMap);

        propsMap.forEach((k, propertyDescriptor) -> {
            Object target = null;
            try {
                target = propertyDescriptor.getReadMethod().invoke(t);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                target = null;
            }
            sqlTool.setEqual(table.get(k).getFullName()).appendParam(target);
        });

        return setUpdateCondition(table, t, propsMap, sqlTool);
    }

    @Override
    public <T extends TableModel> SQLTool updateByPrimaryKeyOptional(BaseTableInfo table, T t, Collection<String> columns) {
        Map<String, PropertyDescriptor> propsMap = PropertyKit.getCachedProps(table);
        SQLTool sqlTool = getUpdateByPrimerySQLTool(table, t, propsMap);

        propsMap.forEach((k, propertyDescriptor) -> {
            if (table.getPrimaryKeys().contains(k) || !columns.contains(k)) {
                return;
            }
            Object target = null;
            try {
                target = propertyDescriptor.getReadMethod().invoke(t);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                target = null;
            }
            sqlTool.setEqual(table.get(k).getFullName()).appendParam(target);
        });
        return setUpdateCondition(table, t, propsMap, sqlTool);
    }


    @Override
    public <T extends TableModel> SQLTool updateByPrimaryKeySelective(BaseTableInfo table, T t) {

        Map<String, PropertyDescriptor> propsMap = PropertyKit.getCachedProps(table);
        SQLTool sqlTool = getUpdateByPrimerySQLTool(table, t, propsMap);

        propsMap.forEach((k, propertyDescriptor) -> {
            if (table.getPrimaryKeys().contains(k)) {
                return;
            }
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
            sqlTool.setEqual(table.get(k).getFullName()).appendParam(target);
        });
        return setUpdateCondition(table, t, propsMap, sqlTool);
    }

    @Override
    public SQLTool deleteByPrimaryKey(BaseTableInfo table, Object id) {
        SQLTool sqlTool = new SQLTool();
        if (table.getPrimaryKeys().size() == 0) return sqlTool;
        sqlTool.delete(table.getTableName()).whereEquals(table.getPrimaryKeys().get(0)).appendParam(id);
        return sqlTool;
    }

    @Override
    public SQLTool deleteByPrimaryKeys(BaseTableInfo table, Collection<Object> ids) {
        SQLTool sqlTool = new SQLTool();
        if (table.getPrimaryKeys().size() == 0) return sqlTool;
        sqlTool.delete(table.getTableName())
                .whereIn(table.getPrimaryKeys().get(0), ids.size());
        ids.forEach(sqlTool::appendParam);
        return sqlTool;
    }

    @Override
    public SQLTool queryAll(BaseTableInfo tableInfo) {
        return new SQLTool().select(tableInfo.getTableName(), tableInfo.get("*").getSelectFullName());
    }

    @Override
    public SQLTool deleteAll(BaseTableInfo tableInfo) {
        return new SQLTool().delete(tableInfo.tableName);
    }


    public static <T extends TableModel> SQLTool getUpdateByPrimerySQLTool(BaseTableInfo table, T t, Map<String, PropertyDescriptor> propsMap) {
        SQLTool sqlTool = new SQLTool();
        sqlTool.update(table.getTableName());
        return sqlTool;
    }

    public static <T extends TableModel> SQLTool setUpdateCondition(BaseTableInfo table, T t, Map<String, PropertyDescriptor> propsMap, SQLTool sqlTool) {
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


}
