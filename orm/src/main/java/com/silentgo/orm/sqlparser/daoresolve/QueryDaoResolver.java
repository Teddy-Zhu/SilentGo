package com.silentgo.orm.sqlparser.daoresolve;

import com.silentgo.orm.base.BaseDaoDialect;
import com.silentgo.orm.base.BaseTableInfo;
import com.silentgo.orm.base.SQLTool;
import com.silentgo.orm.base.TableModel;
import com.silentgo.orm.sqlparser.SQLKit;
import com.silentgo.orm.sqlparser.annotation.ColumnIgnore;
import com.silentgo.orm.sqlparser.annotation.OrderBy;
import com.silentgo.orm.sqlparser.funcanalyse.DaoKeyWord;
import com.silentgo.utils.Assert;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Project : parent
 * Package : com.silentgo.orm.sqlparser.daoresolve
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
    public <T extends TableModel> SQLTool processSQL(String methodName, Class<?> returnType, Object[] objects, Integer[] objectIndex, List<String> parsedMethod, BaseTableInfo tableInfo, SQLTool sqlTool, List<Annotation> annotations, boolean[] isHandled,
                                                     BaseDaoDialect daoDialect, Map<String, Object> nameObjects) {
        if (isHandled[0]) return sqlTool;
        isHandled[0] = true;
        Integer index = 1;
        String two = DaoResolveKit.getField(parsedMethod, index);
        if (DaoKeyWord.One.equals(two)) {
            sqlTool.limit(1, 1);
        } else if (DaoKeyWord.List.equals(two)) {
            Assert.isTrue(Collection.class.isAssignableFrom(returnType), "Method [" + methodName + "] return type should be collection");
            sqlTool.limitClear();
        }

        Optional<Annotation> opColumnIgnore = annotations.stream().filter(annotation -> annotation.annotationType().equals(ColumnIgnore.class)).findFirst();
        if (opColumnIgnore.isPresent()) {
            ColumnIgnore columnIgnore = (ColumnIgnore) opColumnIgnore.get();
            if (columnIgnore.value().length > 0) {
                sqlTool.select(tableInfo.getTableName());
                List<String> ignorelist = Arrays.asList(columnIgnore.value());
                tableInfo.getColumnInfo().entrySet().forEach(column -> {
                    if (!"*".equals(column.getKey()) && !ignorelist.contains(column.getValue().getColumnName())) {
                        sqlTool.selectCol(column.getValue().getSelectFullName());
                    }
                });
            } else {
                sqlTool.select(tableInfo.getTableName(), tableInfo.get("*").getSelectFullName());
            }
        } else {
            sqlTool.select(tableInfo.getTableName(), tableInfo.get("*").getSelectFullName());
        }
        return sqlTool;
    }

}
