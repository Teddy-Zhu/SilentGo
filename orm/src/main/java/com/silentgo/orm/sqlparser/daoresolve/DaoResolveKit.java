package com.silentgo.orm.sqlparser.daoresolve;

import com.silentgo.orm.base.BaseTableInfo;
import com.silentgo.orm.base.Column;
import com.silentgo.utils.StringKit;
import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;

import java.util.List;

/**
 * Project : parent
 * Package : com.silentgo.orm.sqlparser.daoresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/8.
 */
public class DaoResolveKit {

    private static final Log LOGGER = LogFactory.get();
    public static String getField(List<String> parsedString, int index) {
        return index < parsedString.size() ? parsedString.get(index) : "";
    }


    public static String getField(List<String> parsedString, BaseTableInfo baseTableInfo, int index) {
        String field = getField(parsedString, index);
        return getField(field, baseTableInfo);
    }

    public static String getField(String field, BaseTableInfo baseTableInfo) {
        Column column = baseTableInfo.get(StringKit.firstToLower(field));
        String f = column == null ? null : column.getFullName();
        if (StringKit.isNotBlank(f)) {
            return f;
        } else {
            LOGGER.warn("the table [{}] do not contains column [{}]", baseTableInfo.getTableName(), field);
            return null;
        }
    }

    public static boolean isField(String field, BaseTableInfo baseTableInfo) {
        return !StringKit.isBlank(field) && baseTableInfo.get(StringKit.firstToLower(field)) != null;
    }
}
