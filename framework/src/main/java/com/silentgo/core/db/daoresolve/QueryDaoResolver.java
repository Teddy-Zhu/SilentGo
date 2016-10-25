package com.silentgo.core.db.daoresolve;

import com.silentgo.core.db.annotation.Query;
import com.silentgo.core.db.annotation.Select;
import com.silentgo.core.db.funcanalyse.AnalyseKit;
import com.silentgo.orm.base.BaseTableInfo;
import com.silentgo.orm.base.TableModel;
import com.silentgo.core.db.funcanalyse.DaoKeyWord;
import com.silentgo.core.exception.AppSQLException;
import com.silentgo.orm.base.SQLTool;
import com.silentgo.utils.Assert;
import com.silentgo.utils.StringKit;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
        String two = DaoResolveKit.getField(parsedMethod, index);
        if (DaoKeyWord.One.equals(two)) {
            sqlTool.limit(1, 1);
            index += 1;
        } else if (DaoKeyWord.List.equals(two)) {
            Assert.isTrue(Collection.class.isAssignableFrom(returnType), "Method [" + methodName + "] return type should be collection");
            sqlTool.limitClear();
            index += 1;
        }
        if (index != 1) {
            two = DaoResolveKit.getField(parsedMethod, index);
        }
        if (DaoKeyWord.By.equals(two)) {
            setWhere(index, DaoKeyWord.And.innername, parsedMethod, tableInfo, sqlTool);
        }
        sqlTool.select(tableInfo.getTableName(), tableInfo.get("*").getSelectFullName());
        return sqlTool;
    }

    private void setWhere(int index, String string, List<String> parsedMethod, BaseTableInfo tableInfo, SQLTool sqlTool) throws AppSQLException {
        if (DaoKeyWord.And.equals(string)) {
            String f = DaoResolveKit.getField(parsedMethod, tableInfo, index + 1);
            String condition = DaoResolveKit.getField(parsedMethod, index + 2);
            String condition2 = DaoResolveKit.getField(parsedMethod, index + 3);

            if (DaoKeyWord.Greater.equals(condition)) {
                if (DaoKeyWord.Eq.equals(condition2)) {
                    sqlTool.whereGreaterEq(f);
                    index += 2;
                } else {
                    sqlTool.whereGreater(f);
                    index += 1;
                }
            } else if (DaoKeyWord.Less.equals(condition)) {
                if (DaoKeyWord.Eq.equals(condition2)) {
                    sqlTool.whereLessEq(f);
                    index += 2;
                } else {
                    sqlTool.whereLess(f);
                    index += 1;
                }
            } else {
                sqlTool.whereEquals(f);
                index += 1;
            }
            Integer nextIndex = index + 1;
            setWhere(nextIndex, DaoResolveKit.getField(parsedMethod, nextIndex), parsedMethod, tableInfo, sqlTool);
        } else {
            return;
        }
    }
}
