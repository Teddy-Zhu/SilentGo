package com.silentgo.orm.sqlparser.daoresolve;

import com.silentgo.orm.base.DaoResolveEntity;
import com.silentgo.orm.base.TableModel;
import com.silentgo.orm.sqlparser.annotation.LeftJoin;

/**
 * Project : SilentGo
 * Package : com.silentgo.orm.sqlparser.daoresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by teddyzhu on 2016/11/12.
 */
public class LeftJoinDaoResolver implements DaoResolver {
    @Override
    public boolean handle(DaoResolveEntity daoResolveEntity) {
        return daoResolveEntity.getSgMethod().getAnnotationMap().containsKey(LeftJoin.class);
    }

    @Override
    public <T extends TableModel> void processSQL(DaoResolveEntity daoResolveEntity) {
        LeftJoin leftJoin = daoResolveEntity.getSgMethod().getAnnotation(LeftJoin.class);

        daoResolveEntity.getSqlTool().leftJoin(leftJoin.value(), leftJoin.on());
    }
}
