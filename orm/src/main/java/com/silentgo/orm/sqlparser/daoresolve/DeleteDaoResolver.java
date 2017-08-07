package com.silentgo.orm.sqlparser.daoresolve;

import com.silentgo.orm.base.DaoResolveEntity;
import com.silentgo.orm.base.TableModel;
import com.silentgo.orm.sqlparser.funcanalyse.DaoKeyWord;

/**
 * Project : parent
 * Package : com.silentgo.orm.sqlparser.daoresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by teddyzhu on 2016/10/2.
 */
public class DeleteDaoResolver implements DaoResolver {
    @Override
    public boolean handle(DaoResolveEntity daoResolveEntity) {
        return DaoKeyWord.Delete.equals(daoResolveEntity.getParsedMethod().get(0));
    }

    @Override
    public <T extends TableModel> void processSQL(DaoResolveEntity daoResolveEntity) {
        if (daoResolveEntity.getHandled()) return;
        daoResolveEntity.resolved().getSqlTool().delete(daoResolveEntity.getTableInfo().getTableName());
    }
}
