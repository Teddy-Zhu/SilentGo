package com.silentgo.orm.sqlparser.daoresolve;

import com.silentgo.orm.base.BaseTableInfo;
import com.silentgo.orm.base.DaoResolveEntity;
import com.silentgo.orm.base.TableModel;
import com.silentgo.orm.sqlparser.annotation.ColumnIgnore;
import com.silentgo.orm.sqlparser.annotation.Query;
import com.silentgo.orm.sqlparser.funcanalyse.DaoKeyWord;
import com.silentgo.utils.Assert;
import com.silentgo.utils.CollectionKit;
import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Project : parent
 * Package : com.silentgo.orm.sqlparser.daoresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by teddyzhu on 16/9/30.
 */
public class QueryDaoResolver implements DaoResolver {

    private static final Log LOGGER = LogFactory.get();

    private static final Map<Method, List<String>> queryColumns = new HashMap<>();


    @Override
    public boolean handle(DaoResolveEntity daoResolveEntity) {
        return DaoKeyWord.Query.equals(daoResolveEntity.getParsedMethod().get(0));
    }

    @Override
    public <T extends TableModel> void processSQL(DaoResolveEntity daoResolveEntity) {
        if (daoResolveEntity.getHandled()) return;

        BaseTableInfo tableInfo = daoResolveEntity.getTableInfo();


        daoResolveEntity.resolved().getSqlTool().select(tableInfo.getTableName());
        Integer index = 1;
        String two = DaoResolveKit.getField(daoResolveEntity.getParsedMethod(), index);
        if (DaoKeyWord.One.equals(two)) {
            daoResolveEntity.getSqlTool().limit(1, 1);
        } else if (DaoKeyWord.List.equals(two)) {
            Assert.isTrue(Collection.class.isAssignableFrom(daoResolveEntity.getReturnType()), "Method [" + daoResolveEntity.getMethodName() + "] return type should be collection");
            daoResolveEntity.getSqlTool().limitClear();
        }
        boolean needColumns = true;

        Query query = daoResolveEntity.getSgMethod().getAnnotation(Query.class);
        if (query != null) {
            needColumns = query.includeAll();
            for (String s : query.value()) {
                String column = s.trim();
                if (tableInfo.getColumnInfo().containsKey(column))
                    daoResolveEntity.getSqlTool().selectCol(tableInfo.getColumnInfo().get(column).getSelectFullName());
                else if (tableInfo.getOriginColumn().containsKey(column))
                    daoResolveEntity.getSqlTool().selectCol(tableInfo.getOriginColumn().get(column).getSelectFullName());
                else
                    daoResolveEntity.getSqlTool().selectCol(column);
            }
        }
        if (!needColumns) return;
        List<String> queryColumnList = queryColumns.get(daoResolveEntity.getMethod());

        if (!CollectionKit.isEmpty(queryColumnList)) {
            for (String s : queryColumnList) {
                daoResolveEntity.getSqlTool().selectCol(s);
            }
        } else {
            queryColumnList = new ArrayList<>();
            queryColumns.put(daoResolveEntity.getMethod(), queryColumnList);

            ColumnIgnore columnIgnore = (ColumnIgnore) daoResolveEntity.getSgMethod().getAnnotation(ColumnIgnore.class);
            if (columnIgnore != null) {
                if (columnIgnore.value().length > 0) {
                    daoResolveEntity.getSqlTool().select(tableInfo.getTableName());
                    List<String> ignorelist = Arrays.asList(columnIgnore.value());
                    List<String> finalQueryColumnList = queryColumnList;
                    tableInfo.getColumnInfo().forEach((key, value) -> {
                        if (!"*".equals(key) && !ignorelist.contains(value.getColumnName())) {
                            finalQueryColumnList.add(value.getSelectFullName());
                            daoResolveEntity.getSqlTool().selectCol(value.getSelectFullName());
                        }
                    });
                } else {
                    daoResolveEntity.getSqlTool().selectCol(tableInfo.get("*").getSelectFullName());
                    queryColumnList.add(tableInfo.get("*").getSelectFullName());
                }
            } else {
                daoResolveEntity.getSqlTool().selectCol(tableInfo.get("*").getSelectFullName());
                queryColumnList.add(tableInfo.get("*").getSelectFullName());
            }
        }
    }

}
