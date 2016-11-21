package com.silentgo.orm.sqlparser.daoresolve;

import com.silentgo.orm.base.BaseDaoDialect;
import com.silentgo.orm.base.BaseTableInfo;
import com.silentgo.orm.base.SQLTool;
import com.silentgo.orm.base.TableModel;
import com.silentgo.orm.sqlparser.funcanalyse.DaoKeyWord;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

/**
 * Project : parent
 * Package : com.silentgo.orm.sqlparser.daoresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/2.
 */
public class UpdateDaoResolver implements DaoResolver {
    @Override
    public boolean handle(String methodName, List<String> parsedMethod, List<Annotation> annotations) {
        return DaoKeyWord.Update.equals(parsedMethod.get(0));
    }

    @Override
    public <T extends TableModel> SQLTool processSQL(String methodName, Class<?> returnType, Object[] objects, Integer[] objectIndex, List<String> parsedMethod, BaseTableInfo tableInfo, SQLTool sqlTool, List<Annotation> annotations, boolean[] isHandled,
                                                     BaseDaoDialect daoDialect, Map<String, Object> nameObjects) {
        if (isHandled[0]) return sqlTool;
        isHandled[0] = true;
        sqlTool.update(tableInfo.getTableName());

        return sqlTool;
    }

}