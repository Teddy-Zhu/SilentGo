package com.silentgo.core.db.daoresolve;

import com.silentgo.core.db.BaseTableInfo;
import com.silentgo.core.db.TableModel;
import com.silentgo.core.exception.AppSQLException;
import com.silentgo.core.plugin.db.bridge.mysql.SQLTool;

import java.util.List;

/**
 * Project : parent
 * Package : com.silentgo.core.db.daoresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/28.
 */
public interface DaoResolver {

    public boolean handle(String methodName, List<String> parsedMethod);

    public <T extends TableModel> SQLTool processSQL(String methodName,
                                                     Class<?> returnType, Object[] objects,
                                                     List<String> parsedMethod,
                                                     BaseTableInfo tableInfo,
                                                     SQLTool sqlTool,
                                                     boolean[] isHandled) throws AppSQLException;
}
