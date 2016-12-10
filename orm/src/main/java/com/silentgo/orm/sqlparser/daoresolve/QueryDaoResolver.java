package com.silentgo.orm.sqlparser.daoresolve;

import com.silentgo.orm.base.BaseDaoDialect;
import com.silentgo.orm.base.BaseTableInfo;
import com.silentgo.orm.base.SQLTool;
import com.silentgo.orm.base.TableModel;
import com.silentgo.orm.sqlparser.annotation.ColumnIgnore;
import com.silentgo.orm.sqlparser.annotation.Query;
import com.silentgo.orm.sqlparser.funcanalyse.DaoKeyWord;
import com.silentgo.utils.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryDaoResolver.class);

    @Override
    public boolean handle(String methodName, List<String> parsedMethod, List<Annotation> annotations) {
        return DaoKeyWord.Query.equals(parsedMethod.get(0));
    }

    @Override
    public <T extends TableModel> SQLTool processSQL(String methodName, Class<?> returnType, Object[] objects, Integer[] objectIndex, List<String> parsedMethod, BaseTableInfo tableInfo, SQLTool sqlTool, List<Annotation> annotations, boolean[] isHandled,
                                                     BaseDaoDialect daoDialect, Map<String, Object> nameObjects) {
        if (isHandled[0]) return sqlTool;
        isHandled[0] = true;
        sqlTool.select(tableInfo.getTableName());
        Integer index = 1;
        String two = DaoResolveKit.getField(parsedMethod, index);
        if (DaoKeyWord.One.equals(two)) {
            sqlTool.limit(1, 1);
        } else if (DaoKeyWord.List.equals(two)) {
            Assert.isTrue(Collection.class.isAssignableFrom(returnType), "Method [" + methodName + "] return type should be collection");
            sqlTool.limitClear();
        }
        boolean needColumns = true;

        Optional<Annotation> opQuery = annotations.stream().filter(annotation -> annotation.annotationType().equals(Query.class)).findFirst();
        if (opQuery.isPresent()) {
            Query query = (Query) opQuery.get();
            needColumns = query.includeAll();
            for (String s : query.value()) {
                String column = s.trim();
                if (tableInfo.getColumnInfo().containsKey(column))
                    sqlTool.selectCol(tableInfo.getColumnInfo().get(column).getSelectFullName());
                else if (tableInfo.getOriginColumn().containsKey(column))
                    sqlTool.selectCol(tableInfo.getOriginColumn().get(column).getSelectFullName());
                else
                    throw new RuntimeException("error query column [" + s + "]");
            }
        }
        if (!needColumns) return sqlTool;

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
