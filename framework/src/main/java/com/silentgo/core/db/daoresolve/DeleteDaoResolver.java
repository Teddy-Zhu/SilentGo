package com.silentgo.core.db.daoresolve;

import com.silentgo.core.db.BaseTableInfo;
import com.silentgo.core.db.TableModel;
import com.silentgo.core.db.funcanalyse.DaoKeyWord;
import com.silentgo.core.exception.AppSQLException;
import com.silentgo.core.plugin.db.bridge.mysql.SQLTool;
import com.silentgo.utils.StringKit;

import java.util.List;

/**
 * Project : parent
 * Package : com.silentgo.core.db.daoresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/2.
 */
public class DeleteDaoResolver implements DaoResolver {
    @Override
    public boolean handle(String methodName, List<String> parsedMethod) {
        return parsedMethod.contains(DaoKeyWord.Delete.innername);
    }

    @Override
    public <T extends TableModel> SQLTool processSQL(String methodName, Class<?> returnType, Object[] objects, List<String> parsedMethod, BaseTableInfo tableInfo, SQLTool sqlTool, boolean[] isHandled) throws AppSQLException {
        if (isHandled[0]) return sqlTool;
        int index = parsedMethod.indexOf(DaoKeyWord.Order.innername);
        String two = parsedMethod.get(index + 1);
        if (two.equals(DaoKeyWord.By.innername)) {
            String field = parsedMethod.get(index + 2);
            String f = tableInfo.getFullColumns().get(field);
            if (StringKit.isNotBlank(f)) {
                sqlTool.whereEquals(f);
            } else {
                throw new AppSQLException("the table [" + tableInfo.getTableName() + "] do not contains column [" + field + "]");
            }
        } else {
            throw new AppSQLException("error syntax : after Order : " + two);
        }
        return sqlTool.delete(tableInfo.getTableName());
    }
}
