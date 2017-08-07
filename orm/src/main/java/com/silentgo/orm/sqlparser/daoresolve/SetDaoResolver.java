package com.silentgo.orm.sqlparser.daoresolve;

import com.silentgo.orm.base.BaseTableInfo;
import com.silentgo.orm.base.DaoResolveEntity;
import com.silentgo.orm.base.SQLTool;
import com.silentgo.orm.base.TableModel;
import com.silentgo.orm.sqlparser.SQLKit;
import com.silentgo.orm.sqlparser.annotation.Set;
import com.silentgo.orm.sqlparser.funcanalyse.DaoKeyWord;

import java.util.List;

/**
 * Project : SilentGo
 * Package : com.silentgo.orm.sqlparser.daoresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by teddyzhu on 2016/10/28.
 */
public class SetDaoResolver implements DaoResolver {
    @Override
    public boolean handle(DaoResolveEntity daoResolveEntity) {
        return daoResolveEntity.getParsedMethod().contains(DaoKeyWord.Set.innername)
                || daoResolveEntity.getSgMethod().containsAnnotation(Set.class);
    }

    @Override
    public <T extends TableModel> void processSQL(DaoResolveEntity daoResolveEntity) {
        List<String> parsedMethod = daoResolveEntity.getParsedMethod();
        BaseTableInfo tableInfo = daoResolveEntity.getTableInfo();

        int set = parsedMethod.indexOf(DaoKeyWord.Set.innername);
        String filed = DaoResolveKit.getField(parsedMethod, set + 1);
        if (DaoResolveKit.isField(filed, tableInfo))
            set(set, DaoKeyWord.And.innername, daoResolveEntity);

        Set setAn = daoResolveEntity.getSgMethod().getAnnotation(Set.class);
        if (setAn != null) {
            daoResolveEntity.getSqlTool().set(setAn.value());
        }
    }


    public void set(int index, String string, DaoResolveEntity daoResolveEntity) {
        if (DaoKeyWord.And.equals(string)) {
            String field = DaoResolveKit.getField(daoResolveEntity.getParsedMethod(), daoResolveEntity.getTableInfo(), index + 1);
            daoResolveEntity.getSqlTool().set(field + "=<#" + SQLKit.resolveObject(daoResolveEntity) + "/>");
            Integer next = index + 2;
            set(next, daoResolveEntity.getParsedMethod().get(next), daoResolveEntity);
        }
    }
}
