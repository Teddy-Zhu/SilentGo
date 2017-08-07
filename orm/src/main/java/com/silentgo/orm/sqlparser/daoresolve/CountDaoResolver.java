package com.silentgo.orm.sqlparser.daoresolve;

import com.silentgo.orm.base.DaoResolveEntity;
import com.silentgo.orm.base.TableModel;
import com.silentgo.orm.sqlparser.funcanalyse.DaoKeyWord;

/**
 * Project : SilentGo
 * Package : com.silentgo.orm.sqlparser.daoresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by teddyzhu on 2016/10/10.
 */
public class CountDaoResolver implements DaoResolver {
    @Override
    public boolean handle(DaoResolveEntity daoResolveEntity) {
        return DaoKeyWord.Count.equals(daoResolveEntity.getParsedMethod().get(0));
    }

    @Override
    public <T extends TableModel> void processSQL(DaoResolveEntity daoResolveEntity) {
        daoResolveEntity.resolved().getSqlTool().count(daoResolveEntity.getTableInfo().getTableName());
    }
}
