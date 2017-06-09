package com.silentgo.orm.sqlparser.daoresolve;

import com.silentgo.orm.base.BaseDaoDialect;
import com.silentgo.orm.base.BaseTableInfo;
import com.silentgo.orm.base.SQLTool;
import com.silentgo.orm.base.TableModel;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Project : parent
 * Package : com.silentgo.orm.sqlparser.daoresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/28.
 */
public class CommonDaoResolver implements DaoResolver {
    private static final ArrayList<String> methodNames = new ArrayList() {{
        add("queryByPrimaryKey");
        add("queryByPrimaryKeys");
        add("queryByModelSelective");
        add("queryByModelMap");
        add("insertByRow");
        add("insertByRows");
        add("updateByPrimaryKey");
        add("updateByPrimaryKeyOptional");
        add("updateByPrimaryKeySelective");
        add("deleteByPrimaryKey");
        add("deleteByPrimaryKeys");
        add("queryAll");
        add("deleteAll");
        add("queryCustom");
        add("countCustom");
        add("countByModelMap");
    }};

    @Override
    public boolean handle(String methodName, List<String> parsedMethod, List<Annotation> annotations) {
        return methodNames.contains(methodName);
    }

    @Override
    public <T extends TableModel> SQLTool processSQL(String methodName, Class<?> returnType, Object[] objects, Integer[] objectIndex, List<String> parsedMethod, BaseTableInfo tableInfo, SQLTool sqlTool, List<Annotation> annotations, boolean[] isHandled, BaseDaoDialect daoDialect, Map<String, Object> nameObjects) {
        isHandled[0] = true;
        switch (methodName) {
            case "queryByPrimaryKey": {
                return daoDialect.queryByPrimaryKey(tableInfo, objects[0]);
            }
            case "queryByPrimaryKeys": {
                return daoDialect.queryByPrimaryKeys(tableInfo, (Collection<Object>) objects[0]);
            }
            case "queryByModelSelective": {
                return daoDialect.queryByModelSelective(tableInfo, (T) objects[0]);
            }
            case "queryByModelMap": {
                return daoDialect.queryByModelMap(tableInfo, (Map<String, Object>) objects[0]);
            }
            case "insertByRow": {
                return daoDialect.insertByRow(tableInfo, (T) objects[0]);
            }
            case "insertByRows": {
                return daoDialect.insertByRows(tableInfo, (Collection<T>) objects[0]);
            }
            case "updateByPrimaryKey": {

                return daoDialect.updateByPrimaryKey(tableInfo, (T) objects[0]);
            }
            case "updateByPrimaryKeyOptional": {
                return daoDialect.updateByPrimaryKeyOptional(tableInfo, (T) objects[0], (Collection<String>) objects[1]);
            }
            case "updateByPrimaryKeySelective": {
                return daoDialect.updateByPrimaryKeySelective(tableInfo, (T) objects[0]);
            }
            case "deleteByPrimaryKey": {
                return daoDialect.deleteByPrimaryKey(tableInfo, objects[0]);
            }
            case "deleteByPrimaryKeys": {
                return daoDialect.deleteByPrimaryKeys(tableInfo, (Collection<Object>) objects[0]);
            }
            case "queryAll": {
                return daoDialect.queryAll(tableInfo);
            }
            case "deleteAll": {
                return daoDialect.deleteAll(tableInfo);
            }
            case "queryCustom": {
                return (SQLTool) objects[0];
            }
            case "countCustom": {
                return (SQLTool) objects[0];
            }
            case "countByModelMap": {
                return daoDialect.countByModelMap(tableInfo, (Map<String, Object>) objects[0]);
            }
        }
        isHandled[0] = false;
        return sqlTool;
    }

}
