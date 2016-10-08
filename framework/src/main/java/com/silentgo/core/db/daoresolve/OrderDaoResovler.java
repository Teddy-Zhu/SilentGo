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
 *         Created by teddyzhu on 16/9/30.
 */
public class OrderDaoResovler implements DaoResolver {
    @Override
    public boolean handle(String methodName, List<String> parsedMethod) {
        return parsedMethod.contains(DaoKeyWord.Order.innername);
    }

    @Override
    public <T extends TableModel> SQLTool processSQL(String methodName, Class<?> returnType, Object[] objects, List<String> parsedMethod, BaseTableInfo tableInfo, SQLTool sqlTool, boolean[] isHandled) throws AppSQLException {
        int index = parsedMethod.indexOf(DaoKeyWord.Order.innername);
        String two = parsedMethod.get(index + 1);
        if (DaoKeyWord.By.equals(two)) {
            String f = DaoResolveKit.getField(parsedMethod, tableInfo, index + 2);
            String sort = parsedMethod.get(index + 3);
            if (DaoKeyWord.Desc.equals(sort)) {
                sqlTool.orderByDesc(f);
            } else {
                sqlTool.orderByAsc(f);
            }

        } else {
            throw new AppSQLException("error syntax : after Order : " + two);
        }
        return null;
    }
}
