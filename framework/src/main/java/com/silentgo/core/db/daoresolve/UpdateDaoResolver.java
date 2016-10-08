package com.silentgo.core.db.daoresolve;

import com.silentgo.core.db.BaseTableInfo;
import com.silentgo.core.db.TableModel;
import com.silentgo.core.db.funcanalyse.DaoKeyWord;
import com.silentgo.core.exception.AppSQLException;
import com.silentgo.core.plugin.db.bridge.mysql.SQLTool;

import java.util.List;

/**
 * Project : parent
 * Package : com.silentgo.core.db.daoresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/2.
 */
public class UpdateDaoResolver implements DaoResolver {
    @Override
    public boolean handle(String methodName, List<String> parsedMethod) {
        return DaoKeyWord.Update.equals(parsedMethod.get(0));
    }

    @Override
    public <T extends TableModel> SQLTool processSQL(String methodName, Class<?> returnType, Object[] objects, List<String> parsedMethod, BaseTableInfo tableInfo, SQLTool sqlTool, boolean[] isHandled) throws AppSQLException {
        if (!isHandled[0]) return sqlTool;
        isHandled[0] = true;
        String two = parsedMethod.get(1);
        if (DaoKeyWord.By.equals(two)) {
            String f = DaoResolveKit.getField(parsedMethod, tableInfo, 2);
            sqlTool.whereEquals(f);
        }
        int set = parsedMethod.indexOf("Set");

        if (set == -1 && parsedMethod.size() >= (set + 1)) {
            throw new AppSQLException("can not set field because set keyword not found");
        } else {
            String field = DaoResolveKit.getField(parsedMethod, tableInfo, set + 1);
            sqlTool.set(field);
        }
        sqlTool.update(tableInfo.getTableName());
        return sqlTool;
    }
}
