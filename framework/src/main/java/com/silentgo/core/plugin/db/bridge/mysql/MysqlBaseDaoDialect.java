package com.silentgo.core.plugin.db.bridge.mysql;

import com.silentgo.core.db.BaseDaoDialect;
import com.silentgo.core.db.BaseTableInfo;
import com.silentgo.core.db.TableModel;
import com.silentgo.core.plugin.db.util.PropertyTool;
import com.silentgo.utils.StringKit;
import com.silentgo.utils.logger.Logger;
import com.silentgo.utils.logger.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
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

    private static final Logger LOGGER = LoggerFactory.getLog(MysqlBaseDaoDialect.class);


    @Override
    public SQLTool queryByPrimaryKey(BaseTableInfo table, Object id) {
        if (table.getPrimaryKeys().size() == 0) {
            LOGGER.debug("table {} can not find primary key", table.getTableName());
            throw new RuntimeException("the table did not has primary key");
        }
        SQLTool sqlTool = new SQLTool();
        sqlTool.select(table.getTableName(), table.getFullColumns().get("*"))
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
        sqlTool.select(table.getTableName(), table.getFullColumns().get("*"))
                .whereIn(table.getFullColumns().get(table.getPrimaryKeys().get(0)), ids.size());
        ids.forEach(sqlTool::appendParam);
        return sqlTool;
    }

    @Override
    public <T extends TableModel> SQLTool queryByModelSelective(BaseTableInfo table, T t) {

        SQLTool sqlTool = new SQLTool();
        sqlTool.select(table.getTableName(), table.getFullColumns().get("*"));


        PropertyTool.getCachedProps(table).forEach((k, propertyDescriptor) -> {
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

        PropertyTool.getCachedProps(table).forEach((k, propertyDescriptor) -> {
            Object target = null;
            try {
                target = propertyDescriptor.getReadMethod().invoke(t);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            if (table.getPrimaryKeys().contains(k) && target == null) {
                return;
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
            PropertyTool.getCachedProps(table).forEach((k, propertyDescriptor) -> {
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
        Map<String, PropertyDescriptor> propsMap = PropertyTool.getCachedProps(table);
        SQLTool sqlTool = PropertyTool.getUpdateByPrimerySQLTool(table, t, propsMap);

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
        Map<String, PropertyDescriptor> propsMap = PropertyTool.getCachedProps(table);
        SQLTool sqlTool = PropertyTool.getUpdateByPrimerySQLTool(table, t, propsMap);

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

        Map<String, PropertyDescriptor> propsMap = PropertyTool.getCachedProps(table);
        SQLTool sqlTool = PropertyTool.getUpdateByPrimerySQLTool(table, t, propsMap);

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

    @Override
    public SQLTool queryAll(BaseTableInfo tableInfo) {
        return new SQLTool().select(tableInfo.getTableName(), tableInfo.getTableName() + ".*");
    }

    @Override
    public SQLTool deleteAll(BaseTableInfo tableInfo) {
        return new SQLTool().delete(tableInfo.tableName);
    }

}
