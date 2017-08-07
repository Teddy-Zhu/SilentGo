package com.silentgo.orm.sqlparser.daoresolve;

import com.silentgo.orm.base.DaoResolveEntity;
import com.silentgo.orm.base.SQLTool;
import com.silentgo.orm.base.SQLType;
import com.silentgo.orm.base.TableModel;
import com.silentgo.orm.sqlparser.annotation.Select;

/**
 * Project : parent
 * Package : com.silentgo.orm.sqlparser.daoresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by teddyzhu on 2016/10/8.
 */
public class SelectDaoResolver implements DaoResolver {
    @Override
    public boolean handle(DaoResolveEntity daoResolveEntity) {
        return daoResolveEntity.getSgMethod().getAnnotationMap().containsKey(Select.class);
    }

    @Override
    public <T extends TableModel> void processSQL(DaoResolveEntity daoResolveEntity) {

        daoResolveEntity.resolved();

        Select select = daoResolveEntity.getSgMethod().getAnnotation(Select.class);

        daoResolveEntity.setSqlTool(new SQLTool(SQLType.QUERY, select.value()));

    }
}
