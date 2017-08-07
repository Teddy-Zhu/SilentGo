package com.silentgo.orm.sqlparser.daoresolve;

import com.silentgo.orm.base.BaseTableInfo;
import com.silentgo.orm.base.DaoResolveEntity;
import com.silentgo.orm.base.SQLTool;
import com.silentgo.orm.base.TableModel;
import com.silentgo.orm.sqlparser.annotation.OrderBy;
import com.silentgo.orm.sqlparser.funcanalyse.DaoKeyWord;

import java.util.List;

/**
 * Project : parent
 * Package : com.silentgo.orm.sqlparser.daoresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by teddyzhu on 16/9/30.
 */
public class OrderDaoResovler implements DaoResolver {
    @Override
    public boolean handle(DaoResolveEntity daoResolveEntity) {
        return daoResolveEntity.getParsedMethod().contains(DaoKeyWord.OrderBy.innername);
    }

    @Override
    public <T extends TableModel> void processSQL(DaoResolveEntity daoResolveEntity) {
        int index = daoResolveEntity.getParsedMethod().indexOf(DaoKeyWord.OrderBy.innername);
        String nextToken = DaoResolveKit.getField(daoResolveEntity.getParsedMethod(), index + 1);

        SQLTool sqlTool = daoResolveEntity.getSqlTool();
        BaseTableInfo tableInfo = daoResolveEntity.getTableInfo();

        if (DaoResolveKit.isField(nextToken, tableInfo))
            setOrder(index, DaoKeyWord.And.innername, daoResolveEntity.getParsedMethod(), tableInfo, sqlTool);

        OrderBy orderBy = daoResolveEntity.getSgMethod().getAnnotation(OrderBy.class);

        if (orderBy != null) {
            sqlTool.orderBy(orderBy.value());
        }
    }

    public void setOrder(int index, String string, List<String> parsedMethod, BaseTableInfo tableInfo, SQLTool sqlTool) {
        if (DaoKeyWord.And.equals(string)) {
            String f = DaoResolveKit.getField(parsedMethod, tableInfo, index + 1);
            String sort = DaoResolveKit.getField(parsedMethod, index + 2);
            if (DaoKeyWord.Desc.equals(sort)) {
                sqlTool.orderByDesc(f);
                index += 1;
            } else {
                sqlTool.orderByAsc(f);
                index += 1;
            }
            Integer nextIndex = index + 2;
            setOrder(nextIndex, DaoResolveKit.getField(parsedMethod, nextIndex), parsedMethod, tableInfo, sqlTool);
        }
    }
}
