package com.silentgo.core.db.daoresolve;

import com.silentgo.orm.base.BaseTableInfo;
import com.silentgo.orm.base.TableModel;
import com.silentgo.core.db.funcanalyse.DaoKeyWord;
import com.silentgo.core.exception.AppSQLException;
import com.silentgo.orm.base.SQLTool;

import java.lang.annotation.Annotation;
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
    public boolean handle(String methodName, List<String> parsedMethod, List<Annotation> annotations) {
        return DaoKeyWord.Delete.equals(parsedMethod.get(0));
    }

    @Override
    public <T extends TableModel> SQLTool processSQL(String methodName, Class<?> returnType,
                                                     Object[] objects, List<String> parsedMethod,
                                                     BaseTableInfo tableInfo, SQLTool sqlTool,
                                                     List<Annotation> annotations,
                                                     boolean[] isHandled) throws AppSQLException {
        if (isHandled[0]) return sqlTool;
        isHandled[0] = true;
        int index = parsedMethod.indexOf(DaoKeyWord.Order.innername);
        String two = DaoResolveKit.getField(parsedMethod, index + 1);
        if (DaoKeyWord.By.equals(two)) {
            String f = DaoResolveKit.getField(parsedMethod, tableInfo, index + 2);
            sqlTool.whereEquals(f);
        } else {
            throw new AppSQLException("error syntax : after Order : " + two);
        }
        return sqlTool.delete(tableInfo.getTableName());
    }
}
