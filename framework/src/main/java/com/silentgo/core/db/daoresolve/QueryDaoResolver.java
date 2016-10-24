package com.silentgo.core.db.daoresolve;

import com.silentgo.orm.base.BaseTableInfo;
import com.silentgo.orm.base.TableModel;
import com.silentgo.core.db.funcanalyse.DaoKeyWord;
import com.silentgo.core.exception.AppSQLException;
import com.silentgo.orm.base.SQLTool;
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
        Integer index = 1;
        String two = parsedMethod.get(index);
        if (DaoKeyWord.One.equals(two)) {
            sqlTool.limit(1, 1);
            index += 1;
        } else if (DaoKeyWord.List.equals(two)) {
            Assert.isTrue(Collection.class.isAssignableFrom(returnType), "Method [" + methodName + "] return type should be collection");
            sqlTool.limitClear();
            index += 1;
        }
        if (index != 1) {
            two = parsedMethod.get(index);
        }
        if (DaoKeyWord.By.equals(two)) {
            String nextKeyword = parsedMethod.get(index + 1);
            if (DaoKeyWord.List.equals(nextKeyword)) {
                if (objects[0] instanceof Collection) {
                    String f = DaoResolveKit.getField(parsedMethod, tableInfo, index + 2);
                    sqlTool.whereIn(f, ((Collection) objects[0]).size());
                } else {
                    throw new AppSQLException("the first parameter is not collection instance");
                }
            } else {
                String f = DaoResolveKit.getField(nextKeyword, tableInfo);
                sqlTool.whereEquals(f);
            }
        }
        sqlTool.select(tableInfo.getTableName(), tableInfo.get("*").getSelectFullName());
        return sqlTool;
    }
}
