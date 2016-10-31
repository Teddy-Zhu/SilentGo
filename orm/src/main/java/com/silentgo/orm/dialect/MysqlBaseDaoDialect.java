package com.silentgo.orm.dialect;

import com.silentgo.orm.base.BaseDaoDialect;
import com.silentgo.orm.base.BaseTableInfo;
import com.silentgo.orm.base.SQLTool;
import com.silentgo.orm.base.TableModel;
import com.silentgo.orm.kit.PropertyTool;
import com.silentgo.utils.StringKit;
import com.silentgo.utils.logger.Logger;
import com.silentgo.utils.logger.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;
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
            sqlTool.whereEquals(table.get(k).getFullName()).appendParam(target);
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
            sqlTool.insert(table.get(k).getFullName()).appendParam(target);
        });
        return sqlTool;
    }

    @Override
    public <T extends TableModel> SQLTool insertByRows(BaseTableInfo table, Collection<T> t) {
        if (t.size() <= 0) return null;

        Iterator iterator = t.iterator();

        SQLTool sqlTool = insertByRow(table, (T) iterator.next());

        while (iterator.hasNext()){
            T cur = (T) iterator.next();
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
            sqlTool.setEqual(table.get(k).getFullName()).appendParam(target);
        });
        return sqlTool;
    }

    @Override
    public <T extends TableModel> SQLTool updateByPrimaryKeyOptional(BaseTableInfo table, T t, Collection<String> columns) {
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
            sqlTool.setEqual(table.get(k).getFullName()).appendParam(target);
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
            sqlTool.setEqual(table.get(k).getFullName()).appendParam(target);
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

}
