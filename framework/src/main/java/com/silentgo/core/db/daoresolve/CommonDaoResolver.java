package com.silentgo.core.db.daoresolve;

import com.silentgo.core.SilentGo;
import com.silentgo.core.db.BaseDaoDialect;
import com.silentgo.core.db.BaseTableInfo;
import com.silentgo.core.db.DialectFactory;
import com.silentgo.core.db.TableModel;
import com.silentgo.core.exception.AppSQLException;
import com.silentgo.core.plugin.db.bridge.mysql.SQLTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Project : parent
 * Package : com.silentgo.core.db.daoresolve
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
        add("insertByRow");
        add("insertByRows");
        add("updateByPrimaryKey");
        add("updateByPrimaryKeyOptional");
        add("updateByPrimaryKeySelective");
        add("deleteByPrimaryKey");
        add("deleteByPrimaryKeys");
        add("queryAll");
        add("deleteAll");
    }};

    @Override
    public boolean handle(String methodName, List<String> parsedMethod) {
        return methodNames.contains(methodName);
    }

    @Override
    public <T extends TableModel> SQLTool processSQL(String methodName, Class<?> returnType, Object[] objects, List<String> parsedMethod, BaseTableInfo tableInfo, SQLTool sqlTool, boolean[] isHandled) throws AppSQLException {
        BaseDaoDialect daoDialect = SilentGo.getInstance().getFactory(DialectFactory.class).getDialect(tableInfo.getType());
        isHandled[0] = true;
        switch (methodName) {
            case "queryByPrimaryKey": {
                return daoDialect.queryByPrimaryKey(tableInfo, objects[0]);
            }
            case "queryByPrimaryKeys": {
                return daoDialect.queryByPrimaryKeys(tableInfo, (List<Object>) objects[0]);
            }
            case "queryByModelSelective": {
                return daoDialect.queryByModelSelective(tableInfo, (T) objects[0]);
            }
            case "insertByRow": {
                return daoDialect.insertByRow(tableInfo, (T) objects[0]);
            }
            case "insertByRows": {
                return daoDialect.insertByRows(tableInfo, (List<T>) objects[0]);
            }
            case "updateByPrimaryKey": {

                return daoDialect.updateByPrimaryKey(tableInfo, (T) objects[0]);
            }
            case "updateByPrimaryKeyOptional": {
                return daoDialect.updateByPrimaryKeyOptional(tableInfo, (T) objects[0], (List<String>) objects[1]);
            }
            case "updateByPrimaryKeySelective": {
                return daoDialect.updateByPrimaryKeySelective(tableInfo, (T) objects[0]);
            }
            case "deleteByPrimaryKey": {
                return daoDialect.deleteByPrimaryKey(tableInfo, objects[0]);
            }
            case "deleteByPrimaryKeys": {
                return daoDialect.deleteByPrimaryKeys(tableInfo, (List<Object>) objects[0]);
            }
            case "queryAll": {
                return daoDialect.queryAll(tableInfo);
            }
            case "deleteAll": {
                return daoDialect.deleteAll(tableInfo);
            }
        }
        isHandled[0] = false;
        return sqlTool;
    }

}
