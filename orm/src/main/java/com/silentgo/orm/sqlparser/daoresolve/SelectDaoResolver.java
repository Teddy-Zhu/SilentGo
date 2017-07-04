package com.silentgo.orm.sqlparser.daoresolve;

import com.silentgo.orm.base.*;
import com.silentgo.orm.sqlparser.SQLKit;
import com.silentgo.orm.sqlparser.annotation.Select;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Project : parent
 * Package : com.silentgo.orm.sqlparser.daoresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/8.
 */
public class SelectDaoResolver implements DaoResolver {
    @Override
    public boolean handle(String methodName, List<String> parsedMethod, List<Annotation> annotations) {
        return annotations.stream().anyMatch(annotation -> Select.class.equals(annotation.annotationType()));
    }

    @Override
    public <T extends TableModel> SQLTool processSQL(String methodName, Class<?> returnType, Object[] objects, Integer[] objectIndex, List<String> parsedMethod, BaseTableInfo tableInfo, SQLTool sqlTool, List<Annotation> annotations, boolean[] isHandled,
                                                     BaseDaoDialect daoDialect, Map<String, Object> nameObjects, Method method) {
        isHandled[0] = true;
        Select select = (Select) annotations.stream().filter(annotation -> Select.class.equals(annotation.annotationType())).findFirst().get();
        sqlTool = new SQLTool();
        sqlTool.setType(SQLType.QUERY);
        sqlTool.setSql(SQLKit.buildParam(select.value(), objectIndex, objects, sqlTool, nameObjects));
        return sqlTool;
    }
}
