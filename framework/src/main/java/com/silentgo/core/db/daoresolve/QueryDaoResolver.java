package com.silentgo.core.db.daoresolve;

import com.silentgo.core.db.BaseTableInfo;
import com.silentgo.core.db.TableModel;
import com.silentgo.core.db.funcanalyse.DaoKeyWord;
import com.silentgo.core.exception.AppSQLException;
import com.silentgo.core.plugin.db.bridge.mysql.SQLTool;
import com.silentgo.utils.Assert;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;

/**
 * Project : parent
 * Package : com.silentgo.core.db.daoresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/30.
 */
public class QueryDaoResolver implements DaoResolver {
    @Override
    public boolean handle(String methodName, List<String> parsedMethod, List<Annotation> annotations) {
        return DaoKeyWord.Query.equals(parsedMethod.get(0));
    }

    @Override
    public <T extends TableModel> SQLTool processSQL(String methodName, Class<?> returnType, Object[] objects, List<String> parsedMethod, BaseTableInfo tableInfo, SQLTool sqlTool, List<Annotation> annotations, boolean[] isHandled) throws AppSQLException {
        if (isHandled[0]) return sqlTool;
        isHandled[0] = true;
        String two = parsedMethod.get(1);
        if (DaoKeyWord.By.equals(two)) {
            String f = DaoResolveKit.getField(parsedMethod, tableInfo, 2);
            sqlTool.whereEquals(f);
        } else if (DaoKeyWord.One.equals(two)) {
            sqlTool.limit(1, 1);
        } else if (DaoKeyWord.List.equals(two)) {
            Assert.isTrue(Collection.class.isAssignableFrom(returnType), "Method [" + methodName + "] return type should be collection");
            sqlTool.limitClear();
        }

        sqlTool.select(tableInfo.getTableName(), tableInfo.getFullColumns().get("*"));
        return sqlTool;
    }
}
