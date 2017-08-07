package com.silentgo.orm.dialect;

import com.silentgo.orm.base.*;
import com.silentgo.orm.kit.PropertyKit;
import com.silentgo.orm.sqlparser.SQLKit;
import com.silentgo.orm.sqlparser.daoresolve.ResolvedParam;
import com.silentgo.orm.sqlparser.daoresolve.parameterresolver.ObjectToNamedDaoParamResolver;
import com.silentgo.orm.sqltool.SqlTokenGroup;
import com.silentgo.orm.sqltool.sqltoken.ForEachSqlToken;
import com.silentgo.orm.sqltool.sqltoken.IfSqlToken;
import com.silentgo.orm.sqltool.sqltoken.ListContainIfSqlToken;
import com.silentgo.orm.sqltool.sqltoken.ParamSqlToken;
import com.silentgo.utils.StringKit;
import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Project : silentgo
 * com.silentgo.core.plugin.db.bridge.mysql
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by teddyzhu on 16/9/22.
 */
public class MysqlBaseDaoDialect implements BaseDaoDialect {

    private static final Log LOGGER = LogFactory.get();

    @Override
    public SQLTool queryByPrimaryKey(BaseTableInfo table, Object id) {
        if (table.getPrimaryKeys().size() == 0) {
            LOGGER.debug("table {} can not find primary key", table.getTableName());
            throw new RuntimeException("the table did not has primary key");
        }
        String name = SQLKit.objectPrefix + "id";
        SQLTool sqlTool = new SQLTool();
        sqlTool.select(table.getTableName(), table.get("*").getSelectFullName())
                .where(table.get(table.getPrimaryKeys().get(0)).getFullName() + "=<#" + name + "/>");

        sqlTool.addParamResolver(new ObjectToNamedDaoParamResolver(new ResolvedParam(name, 0)));
        return sqlTool;
    }

    @Override
    public SQLTool queryByPrimaryKeys(BaseTableInfo table, Collection<Object> ids) {
        if (table.getPrimaryKeys().size() == 0) {
            LOGGER.debug("table {} can not find primary key", table.getTableName());
            throw new RuntimeException("the table did not has primary key");
        }

        String name = SQLKit.objectPrefix + "ids";

        SQLTool sqlTool = new SQLTool();
        sqlTool.select(table.getTableName(), table.get("*").getSelectFullName())
                .where(table.get(table.getPrimaryKeys().get(0)).getFullName() + " in (<#" + name + "/>) ");

        sqlTool.addParamResolver(new ObjectToNamedDaoParamResolver(new ResolvedParam(name, 0)));

        return sqlTool;
    }

    @Override
    public <T extends TableModel> SQLTool queryByModelSelective(BaseTableInfo table, T t) {

        SQLTool sqlTool = new SQLTool();
        sqlTool.select(table.getTableName(), table.get("*").getSelectFullName());


        String name = SQLKit.objectPrefix + "obj";
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
            Column column = table.get(k);

            sqlTool.where(column.getFullName() + "=<#" + name + "." + column.getPropName() + "/>");
        });

        sqlTool.addParamResolver(new ObjectToNamedDaoParamResolver(new ResolvedParam(name, 0)));

        return sqlTool;
    }

    @Override
    public <T extends TableModel> SQLTool queryByModelMap(BaseTableInfo table, Map<String, Object> t) {

        SQLTool sqlTool = new SQLTool();
        sqlTool.select(table.getTableName(), table.get("*").getSelectFullName());

        String name = SQLKit.objectPrefix + "obj";
        PropertyKit.getCachedProps(table).forEach((k, propertyDescriptor) -> {
            Object target = t.get(k);

            if (target == null) {
                return;
            }
            if (target instanceof String && StringKit.isBlank(target.toString())) {
                return;
            }
            Column column = table.get(k);

            sqlTool.where(column.getFullName() + "=<#" + name + "." + column.getPropName() + "/>");

        });

        Object orderBy = t.get("orderBy");

        if (orderBy != null && orderBy instanceof String) {
            sqlTool.orderBy(String.valueOf(orderBy));
        }

        Object limitBy = t.get("limitBy");

        if (limitBy != null && limitBy instanceof String) {
            sqlTool.limit(String.valueOf(limitBy));
        }

        sqlTool.addParamResolver(new ObjectToNamedDaoParamResolver(new ResolvedParam(name, 0)));

        return sqlTool;
    }

    @Override
    public <T extends TableModel> SQLTool insertByRow(BaseTableInfo table, T t) {

        SQLTool sqlTool = new SQLTool().insert(table.getTableName());

        String objName = SQLKit.objectPrefix + "obj";
        SqlTokenGroup sqlTokenGroup = new SqlTokenGroup();

        PropertyKit.getCachedProps(table).forEach((k, propertyDescriptor) -> {
            Object target = null;
            try {
                target = propertyDescriptor.getReadMethod().invoke(t);
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOGGER.error(e);
            }
            Column column = table.get(k);
            if (target == null) {
                if (column.isAutoIncrement()) return;
                if (column.isNullable()) {
                    sqlTool.insertCol(column.getFullName());
                    sqlTokenGroup.appendToken(new ParamSqlToken(objName + "." + column.getPropName()));
                } else {
                    if (column.isHasDefault()) {
                        //ignore
                    } else {
                        throw new RuntimeException("column: " + column.getColumnName() + " can not be null");
                    }
                }
            } else {
                sqlTool.insertCol(column.getFullName());
                sqlTokenGroup.appendToken(new ParamSqlToken(objName + "." + column.getPropName()));
            }
        });
        sqlTool.insertValue(sqlTokenGroup);

        sqlTool.addParamResolver(new ObjectToNamedDaoParamResolver(new ResolvedParam(objName, 0)));

        return sqlTool;
    }

    @Override
    public <T extends TableModel> SQLTool insertByRows(BaseTableInfo table, Collection<T> t) {
        if (t.size() <= 0) return null;

        Iterator iterator = t.iterator();

        SQLTool sqlTool = new SQLTool().insert(table.getTableName());

        Object first = iterator.next();

        String objName = SQLKit.objectPrefix + "obj";


        SqlTokenGroup parentGroup = new SqlTokenGroup();


        SqlTokenGroup sqlTokenGroup = new SqlTokenGroup();
        ForEachSqlToken forEachSqlToken = new ForEachSqlToken(objName, sqlTokenGroup, " ( ", " ) ", ",");
        parentGroup.appendToken(forEachSqlToken);

        PropertyKit.getCachedProps(table).forEach((k, propertyDescriptor) -> {
            Object target = null;
            try {
                target = propertyDescriptor.getReadMethod().invoke(first);
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOGGER.error(e);
            }
            Column column = table.get(k);
            if (target == null) {
                if (column.isAutoIncrement()) return;
                if (column.isNullable()) {
                    sqlTool.insertCol(column.getFullName());
                    sqlTokenGroup.appendToken(new ParamSqlToken(column.getPropName()));
                } else {
                    if (column.isHasDefault()) {
                        //ignore
                    } else {
                        throw new RuntimeException("column: " + column.getColumnName() + " can not be null");
                    }
                }
            } else {
                sqlTool.insertCol(column.getFullName());
                sqlTokenGroup.appendToken(new ParamSqlToken(column.getPropName()));
            }
        });

        sqlTool.appendParam(objName, t);

        sqlTool.insertValue(parentGroup);

        sqlTool.addParamResolver(new ObjectToNamedDaoParamResolver(new ResolvedParam(objName, 0)));

        return sqlTool;
    }

    @Override
    public <T extends TableModel> SQLTool updateByPrimaryKey(BaseTableInfo table, T t) {
        Map<String, PropertyDescriptor> propsMap = PropertyKit.getCachedProps(table);
        SQLTool sqlTool = getUpdateByPrimerySQLTool(table, t, propsMap);
        String objName = SQLKit.objectPrefix + "obj";
        propsMap.forEach((k, propertyDescriptor) -> {
            Column column = table.get(k);
            sqlTool.set(column.getFullName() + "=<#" + objName + "." + column.getPropName() + "/>");
        });

        sqlTool.addParamResolver(new ObjectToNamedDaoParamResolver(new ResolvedParam(objName, 0)));

        return setUpdateCondition(table, t, propsMap, sqlTool);
    }

    @Override
    public <T extends TableModel> SQLTool updateByPrimaryKeyOptional(BaseTableInfo table, T t, Collection<String> columns) {
        Map<String, PropertyDescriptor> propsMap = PropertyKit.getCachedProps(table);
        SQLTool sqlTool = getUpdateByPrimerySQLTool(table, t, propsMap);
        String objName = SQLKit.objectPrefix + "obj";
        String dbObjName = SQLKit.objectPrefix + "col";
        propsMap.forEach((k, propertyDescriptor) -> {
            if (table.getPrimaryKeys().contains(k)) {
                return;
            }
            Column column = table.get(k);
            SqlTokenGroup sqlTokenGroup = new SqlTokenGroup();
            sqlTokenGroup.appendToken(new ListContainIfSqlToken(dbObjName, k, column.getFullName() + "=<#" + objName + "." + column.getPropName() + "/>"));
            sqlTool.set(sqlTokenGroup);
        });

        sqlTool.addParamResolver(new ObjectToNamedDaoParamResolver(new ResolvedParam(objName, 0)));
        sqlTool.addParamResolver(new ObjectToNamedDaoParamResolver(new ResolvedParam(dbObjName, 1)));

        return setUpdateCondition(table, t, propsMap, sqlTool);
    }


    @Override
    public <T extends TableModel> SQLTool updateByPrimaryKeySelective(BaseTableInfo table, T t) {

        Map<String, PropertyDescriptor> propsMap = PropertyKit.getCachedProps(table);
        SQLTool sqlTool = getUpdateByPrimerySQLTool(table, t, propsMap);
        String objName = SQLKit.objectPrefix + "obj";

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
            Column column = table.get(k);
            sqlTool.set(column.getFullName() + "=<#" + objName + "." + column.getPropName() + "/>");
        });

        sqlTool.addParamResolver(new ObjectToNamedDaoParamResolver(new ResolvedParam(objName, 0)));

        return setUpdateCondition(table, t, propsMap, sqlTool);
    }

    @Override
    public SQLTool deleteByPrimaryKey(BaseTableInfo table, Object id) {
        SQLTool sqlTool = new SQLTool();
        if (table.getPrimaryKeys().size() == 0) return sqlTool;
        sqlTool.delete(table.getTableName());

        String objName = SQLKit.objectPrefix + "id";

        String key = table.getPrimaryKeys().get(0);

        sqlTool.where(table.get(key).getFullName() + "=<#" + objName + "/>");

        sqlTool.addParamResolver(new ObjectToNamedDaoParamResolver(new ResolvedParam(objName, 0)));

        return sqlTool;
    }

    @Override
    public SQLTool deleteByPrimaryKeys(BaseTableInfo table, Collection<Object> ids) {
        SQLTool sqlTool = new SQLTool();
        if (table.getPrimaryKeys().size() == 0) return sqlTool;
        sqlTool.delete(table.getTableName());
        String objName = SQLKit.objectPrefix + "ids";

        String key = table.getPrimaryKeys().get(0);
        Column column = table.get(key);
        sqlTool.where(column.getFullName() + " in (<#" + objName + "/>)");

        sqlTool.addParamResolver(new ObjectToNamedDaoParamResolver(new ResolvedParam(objName, 0)));

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

    @Override
    public SQLTool countByModelMap(BaseTableInfo table, Map<String, Object> t) {
        SQLTool sqlTool = new SQLTool();
        sqlTool.count(table.getTableName());
        String objName = SQLKit.objectPrefix + "obj";
        PropertyKit.getCachedProps(table).forEach((k, propertyDescriptor) -> {
            Object target = t.get(k);

            if (target == null) {
                return;
            }
            if (target instanceof String && StringKit.isBlank(target.toString())) {
                return;
            }
            Column column = table.get(k);
            sqlTool.where(column.getFullName() + "=<#" + objName + "." + column.getPropName() + "/>");
        });


        sqlTool.addParamResolver(new ObjectToNamedDaoParamResolver(new ResolvedParam(objName, 0)));

        Object limitBy = t.get("limitBy");

        if (limitBy != null && limitBy instanceof String) {
            sqlTool.limit(String.valueOf(limitBy));
        }

        return sqlTool;
    }


    public static <T extends TableModel> SQLTool getUpdateByPrimerySQLTool(BaseTableInfo table, T t, Map<String, PropertyDescriptor> propsMap) {
        SQLTool sqlTool = new SQLTool();
        sqlTool.update(table.getTableName());
        return sqlTool;
    }

    public static <T extends TableModel> SQLTool setUpdateCondition(BaseTableInfo table, T t, Map<String, PropertyDescriptor> propsMap, SQLTool sqlTool) {
        String objName = SQLKit.objectPrefix + "obj";
        table.getPrimaryKeys().forEach(key -> {

            PropertyDescriptor propertyDescriptor = propsMap.get(key);
            Object target;
            try {
                target = propertyDescriptor.getReadMethod().invoke(t);
            } catch (IllegalAccessException | InvocationTargetException e) {
                target = null;
            }
            Column column = table.get(key);
            sqlTool.where(key + "=<#" + objName + "." + column.getPropName() + "/>");
        });
        return sqlTool;
    }


}
