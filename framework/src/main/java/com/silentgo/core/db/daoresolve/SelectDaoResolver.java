package com.silentgo.core.db.daoresolve;

import com.silentgo.orm.base.BaseTableInfo;
import com.silentgo.orm.base.TableModel;
import com.silentgo.core.db.annotation.Select;
import com.silentgo.core.exception.AppSQLException;
import com.silentgo.orm.base.SQLTool;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

/**
 * Project : parent
 * Package : com.silentgo.core.db.daoresolve
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
    public <T extends TableModel> SQLTool processSQL(String methodName, Class<?> returnType, Object[] objects, List<String> parsedMethod, BaseTableInfo tableInfo, SQLTool sqlTool, List<Annotation> annotations, boolean[] isHandled) throws AppSQLException {
        isHandled[0] = true;
        Select select = (Select) annotations.stream().filter(annotation -> Select.class.equals(annotation.annotationType())).findFirst().get();
        return new SQLTool(select.value(), Arrays.asList(objects));
    }
}