package com.silentgo.orm.sqlparser.daoresolve;

import com.silentgo.orm.base.BaseTableInfo;
import com.silentgo.orm.base.DaoResolveEntity;
import com.silentgo.orm.base.SQLTool;
import com.silentgo.orm.base.TableModel;
import com.silentgo.orm.sqlparser.SQLKit;
import com.silentgo.orm.sqlparser.annotation.GroupBy;
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
public class GroupDaoResovler implements DaoResolver {
    @Override
    public boolean handle(DaoResolveEntity daoResolveEntity) {
        return daoResolveEntity.getParsedMethod().contains(DaoKeyWord.GroupBy.innername);
    }

    @Override
    public <T extends TableModel> void processSQL(DaoResolveEntity daoResolveEntity) {
        List<String> parsedMethod = daoResolveEntity.getParsedMethod();
        BaseTableInfo tableInfo = daoResolveEntity.getTableInfo();
        SQLTool sqlTool = daoResolveEntity.getSqlTool();

        int index = parsedMethod.indexOf(DaoKeyWord.GroupBy.innername);
        String nextToken = DaoResolveKit.getField(parsedMethod, index + 1);

        if (DaoResolveKit.isField(nextToken, tableInfo))
            setGroup(index + 1, DaoKeyWord.And.innername, parsedMethod, tableInfo, sqlTool);

        GroupBy groupBy = daoResolveEntity.getSgMethod().getAnnotation(GroupBy.class);
        if (groupBy != null) {
            sqlTool.groupBy(groupBy.value());
        }
    }

    public void setGroup(int index, String string, List<String> parsedMethod, BaseTableInfo tableInfo, SQLTool sqlTool) {
        if (DaoKeyWord.And.equals(string)) {
            String f = DaoResolveKit.getField(parsedMethod, tableInfo, index + 1);
            sqlTool.groupBy(f);
            Integer nextIndex = index + 2;
            setGroup(nextIndex, DaoResolveKit.getField(parsedMethod, nextIndex), parsedMethod, tableInfo, sqlTool);
        }
    }
}
