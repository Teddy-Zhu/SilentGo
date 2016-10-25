package com.silentgo.core.db.daoresolve;

import com.silentgo.orm.base.BaseTableInfo;
import com.silentgo.core.exception.AppSQLException;
import com.silentgo.orm.base.Column;
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

    public static String getField(List<String> parsedString, int index) throws AppSQLException {
        return index < parsedString.size() ? parsedString.get(index) : "";
    }


    public static String getField(List<String> parsedString, BaseTableInfo baseTableInfo, int index) throws AppSQLException {
        String field = getField(parsedString, index);
        return getField(field, baseTableInfo);
    }

    public static String getField(String field, BaseTableInfo baseTableInfo) throws AppSQLException {
        Column column = baseTableInfo.get(StringKit.firstToLower(field));
        String f = column == null ? null : column.getFullName();
        if (StringKit.isNotBlank(f)) {
            return f;
        } else {
            throw new AppSQLException("the table [" + baseTableInfo.getTableName() + "] do not contains column [" + field + "]");
        }
    }

    public static boolean isField(String field, BaseTableInfo baseTableInfo) {
        return baseTableInfo.get(StringKit.firstToLower(field)) != null;
    }
}
