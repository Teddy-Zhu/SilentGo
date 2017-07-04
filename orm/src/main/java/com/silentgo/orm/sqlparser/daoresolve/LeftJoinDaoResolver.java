package com.silentgo.orm.sqlparser.daoresolve;

import com.silentgo.orm.base.BaseDaoDialect;
import com.silentgo.orm.base.BaseTableInfo;
import com.silentgo.orm.base.SQLTool;
import com.silentgo.orm.base.TableModel;
import com.silentgo.orm.sqlparser.annotation.LeftJoin;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Project : SilentGo
 * Package : com.silentgo.orm.sqlparser.daoresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/11/12.
 */
public class LeftJoinDaoResolver implements DaoResolver {
    @Override
    public boolean handle(String methodName, List<String> parsedMethod, List<Annotation> annotations) {
        return annotations.stream().anyMatch(annotation -> annotation.annotationType().equals(LeftJoin.class));
    }

    @Override
    public <T extends TableModel> SQLTool processSQL(String methodName, Class<?> returnType, Object[] objects, Integer[] objectIndex, List<String> parsedMethod, BaseTableInfo tableInfo, SQLTool sqlTool, List<Annotation> annotations, boolean[] isHandled, BaseDaoDialect daoDialect, Map<String, Object> nameObjects, Method method) {
        LeftJoin leftJoin = (LeftJoin) annotations.stream().filter(annotation -> annotation.annotationType().equals(LeftJoin.class)).findFirst().get();

        sqlTool.leftJoin(leftJoin.value(), leftJoin.on());
        return sqlTool;
    }
}
