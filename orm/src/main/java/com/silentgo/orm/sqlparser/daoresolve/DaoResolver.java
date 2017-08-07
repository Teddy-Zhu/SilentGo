package com.silentgo.orm.sqlparser.daoresolve;

import com.silentgo.orm.base.*;

/**
 * Project : parent
 * Package : com.silentgo.orm.sqlparser.daoresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/28.
 */
public interface DaoResolver {

    public boolean handle(DaoResolveEntity daoResolveEntity);

    public <T extends TableModel> void processSQL(DaoResolveEntity daoResolveEntity);
}
