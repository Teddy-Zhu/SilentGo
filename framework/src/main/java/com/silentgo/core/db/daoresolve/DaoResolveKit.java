package com.silentgo.core.db.daoresolve;

import com.silentgo.core.db.BaseTableInfo;
import com.silentgo.core.exception.AppSQLException;
import com.silentgo.utils.StringKit;

import java.util.List;

/**
 * Project : parent
 * Package : com.silentgo.core.db.daoresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/8.
 */
public class DaoResolveKit {

    public static String getField(List<String> parsedString, BaseTableInfo baseTableInfo, int index) throws AppSQLException {
        String field = parsedString.get(index);
        String f = baseTableInfo.getFullColumns().get(field);
        if (StringKit.isNotBlank(f)) {
            return f;
        } else {
            throw new AppSQLException("the table [" + baseTableInfo.getTableName() + "] do not contains column [" + field + "]");
        }
    }
}
